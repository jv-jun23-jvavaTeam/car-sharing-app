package com.jvavateam.carsharingapp.payment;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.exception.EntityNotFoundException;
import com.jvavateam.carsharingapp.exception.PaymentException;
import com.jvavateam.carsharingapp.mapper.payment.PaymentMapper;
import com.jvavateam.carsharingapp.model.Payment;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.notification.NotificationService;
import com.jvavateam.carsharingapp.payment.calculator.PaymentCalculationsHandler;
import com.jvavateam.carsharingapp.payment.calculator.TotalCalculator;
import com.jvavateam.carsharingapp.repository.payment.PaymentRepository;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
import com.jvavateam.carsharingapp.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss"
    );
    private static final String SHARED_PAYMENT_DETAILS = """
            🌟 *Payment Successful* 🌟

            💳 Payment ID: %d
            🚗 Rental ID: %d
            💰 Amount Paid: %s
            📅 Date and Time: %s

            Payment has been successfully processed! 🎉

            Best regards,
            Jvava Car Sharing
            """;

    private static final String CLIENT_PAYMENT_NOTIFICATION = """
            Hello %s! 👋

            Here are the payment details for your recent transaction:

            %s
            """;
    private static final String CURRENCY_NAME = "usd";
    private static final Long MAX_NUMBER_OF_CARS_TO_RENT = 1L;
    private static final Long EXPIRATION_TIME = Instant.now().getEpochSecond() + 86400L;
    private static final Long PRICE_MULTIPLIER = 100L;

    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final PaymentMapper paymentMapper;
    private final UserService userService;
    private final NotificationService notificationService;

    @Value("${base.api.url}")
    private String baseApiUrl;
    @Value("${api.success.endpoint}")
    private String apiSuccessEndpoint;
    @Value("${api.cancel.endpoint}")
    private String apiCancelEndpoint;
    @Value("${stripe.secret.key}")
    private String stripeKey;

    @Override
    public List<PaymentResponseDto> getAllForCurrentUser() {
        return paymentRepository.findAllForCurrentUser().stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public List<PaymentResponseDto> getAllForUser(Long id) {
        return paymentRepository.findAllByRentalUserId(id).stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PaymentResponseDto create(CreatePaymentRequestDto requestDto) {
        Stripe.apiKey = stripeKey;
        existsPendingPayment(requestDto.paymentType());

        Payment payment = paymentMapper.toEntity(requestDto);
        Rental rental = getRentalById(requestDto.rentalId());

        existsPaymentForRental(rental, payment);
        checkRentalDates(rental);

        payment.setRental(rental);

        Session session = createSession(payment);

        payment.setSessionUrl(session.getUrl())
                .setSessionId(session.getId())
                .setStatus(Payment.Status.PENDING)
                .setAmountToPay(BigDecimal.valueOf(countTotal(payment)));

        paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    @Override
    public List<PaymentResponseDto> getAllSuccessful() {
        return paymentRepository.findAllByStatus(Payment.Status.PAID)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public List<PaymentResponseDto> getAllPaused() {
        return paymentRepository.findAllByStatus(Payment.Status.PENDING)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public void updateStatus(String sessionId) {
        Payment payment = getPaymentBySessionId(sessionId);
        payment.setStatus(Payment.Status.PAID);
        paymentRepository.save(payment);

        String message = getMessage(payment);
        List<User> managers = userService.findAllManagers();
        notificationService.notifyAll(managers, message);

        User client = payment.getRental().getUser();
        notificationService.sendMessage(
                client,
                String.format(
                        CLIENT_PAYMENT_NOTIFICATION,
                        client.getFirstName(),
                        message
                ));
    }

    private Payment getPaymentBySessionId(String sessionId) {
        return paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find payment by session id: " + sessionId
                ));
    }

    private Rental getRentalById(Long id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find rental by id: " + id
                ));
    }

    private Session createSession(Payment payment) {
        Product product = getProduct(payment);
        Price price = getPrice(product, payment);
        return getSession(price);
    }

    private Session getSession(Price price) {
        SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(price.getId())
                        .setQuantity(MAX_NUMBER_OF_CARS_TO_RENT)
                        .build())
                .setExpiresAt(EXPIRATION_TIME)
                .setSuccessUrl(baseApiUrl
                               + apiSuccessEndpoint
                               + "?sessionId={CHECKOUT_SESSION_ID}")
                .setCancelUrl(baseApiUrl + apiCancelEndpoint)
                .build();
        try {
            return Session.create(sessionCreateParams);
        } catch (StripeException e) {
            throw new PaymentException("Session creation went wrong!", e);
        }
    }

    private Price getPrice(Product product, Payment payment) {
        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setCurrency(CURRENCY_NAME)
                .setProduct(product.getId())
                .setUnitAmount(countTotal(payment) * PRICE_MULTIPLIER)
                .build();
        try {
            return Price.create(priceParams);
        } catch (StripeException e) {
            throw new PaymentException("Price creation went wrong!", e);
        }
    }

    private Product getProduct(Payment payment) {
        ProductCreateParams productParams = new ProductCreateParams.Builder()
                .setName(getCarName(payment))
                .setDefaultPriceData(ProductCreateParams.DefaultPriceData.builder()
                        .setUnitAmount(countTotal(payment) * PRICE_MULTIPLIER)
                        .setCurrency(CURRENCY_NAME)
                        .build())
                .build();
        try {
            return Product.create(productParams);
        } catch (StripeException e) {
            throw new PaymentException("Product creation went wrong!", e);
        }
    }

    private void checkRentalDates(Rental rental) {
        if (rental.getReturnDate() == null) {
            throw new PaymentException(
                    "You can't carry out payment if return date is null!"
            );
        }
    }

    private void existsPendingPayment(Payment.Type type) {
        boolean check = paymentRepository.findAllForCurrentUser()
                .stream()
                .anyMatch(payment -> payment.getStatus().equals(Payment.Status.PENDING)
                                     && payment.getType().equals(type)
                );
        if (check) {
            throw new PaymentException(
                    "You have unpaid payments! New payment can not be created!"
            );
        }
    }

    private void existsPaymentForRental(Rental rental, Payment payment) {
        boolean check = paymentRepository.findAllForCurrentUser().stream()
                .anyMatch(p -> p.getRental().equals(rental)
                               && p.getType().equals(payment.getType()));
        if (check) {
            throw new PaymentException(
                    "You have already payment with type "
                    + payment.getType()
                    + " for this rental! New payment can not be created!"
            );
        }
    }

    private String getCarName(Payment payment) {
        return payment.getRental().getCar().getModel();
    }

    private Long countTotal(Payment payment) {
        TotalCalculator calculator = PaymentCalculationsHandler.getCalculator(payment);
        return calculator.calculateTotal(payment);
    }

    private String getMessage(Payment payment) {
        return String.format(
                SHARED_PAYMENT_DETAILS,
                payment.getId(),
                payment.getRental().getId(),
                payment.getAmountToPay(),
                LocalDateTime.now().format(DATE_TIME_FORMATTER)
        );
    }
}
