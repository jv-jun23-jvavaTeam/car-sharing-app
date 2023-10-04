package com.jvavateam.carsharingapp.payment;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.exception.EntityNotFoundException;
import com.jvavateam.carsharingapp.exception.PaymentException;
import com.jvavateam.carsharingapp.mapper.payment.PaymentMapper;
import com.jvavateam.carsharingapp.model.Payment;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.payment.calculator.PaymentCalculationsHandler;
import com.jvavateam.carsharingapp.payment.calculator.TotalCalculator;
import com.jvavateam.carsharingapp.repository.payment.PaymentRepository;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String CURRENCY_NAME = "usd";
    private static final Long MAX_NUMBER_OF_CARS_TO_RENT = 1L;
    private static final Long EXPIRATION_TIME = Instant.now().getEpochSecond() + 86400L;
    private static final Long PRICE_MULTIPLIER = 100L;

    private final PaymentRepository paymentRepository;
    private final RentalRepository rentalRepository;
    private final PaymentMapper paymentMapper;
    private TotalCalculator calculator;

    @Value("${baseApiUrl}")
    private String baseApiUrl;
    @Value("${apiSuccessEndpoint}")
    private String apiSuccessEndpoint;
    @Value("${apiCancelEndpoint}")
    private String apiCancelEndpoint;
    @Value("${stripe.secretKey}")
    private String stripeKey;

    @Override
    public List<PaymentResponseDto> getAllForCurrentUser() {
        return paymentRepository.findAll().stream()
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
    public PaymentResponseDto createPayment(CreatePaymentRequestDto requestDto) {
        Stripe.apiKey = stripeKey;
        existsPendingPayment();

        Payment payment = paymentMapper.toEntity(requestDto);
        Rental rental = getRentalById(requestDto.rentalId());

        existsPaymentForRental(rental);

        checkRentalDates(rental);

        payment.setRental(rental);
        Session session = createSession(payment);

        payment.setSessionUrl(session.getUrl());
        payment.setSessionId(session.getId());
        payment.setStatus(Payment.Status.PENDING);
        payment.setAmountToPay(BigDecimal.valueOf(countTotal(payment)));
        paymentRepository.save(payment);
        PaymentResponseDto responseDto = paymentMapper.toDto(payment);
        return responseDto;
    }

    @Override
    public List<PaymentResponseDto> getAllSuccessfulPayments() {
        return paymentRepository.findAllByStatus(Payment.Status.PAID)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public List<PaymentResponseDto> getAllPausedPayments() {
        return paymentRepository.findAllByStatus(Payment.Status.PENDING)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public void updatePaymentStatus(String sessionId) {
        Payment payment = getPaymentBySessionId(sessionId);
        payment.setStatus(Payment.Status.PAID);
        paymentRepository.save(payment);
    }

    private Payment getPaymentBySessionId(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find payment by session id: " + sessionId));
        return payment;
    }

    private Rental getRentalById(Long id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find rental by id: " + id));
        return rental;
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
        Session session = null;
        try {
            session = Session.create(sessionCreateParams);
        } catch (StripeException e) {
            throw new PaymentException("Session " + session.getId() + " creation went wrong!");
        }
        return session;
    }

    private Price getPrice(Product product, Payment payment) {
        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setCurrency(CURRENCY_NAME)
                .setProduct(product.getId())
                .setUnitAmount(countTotal(payment) * PRICE_MULTIPLIER)
                .build();
        Price price = null;
        try {
            price = Price.create(priceParams);
        } catch (StripeException e) {
            throw new PaymentException("Price " + price.getId() + " creation went wrong!");
        }
        return price;
    }

    private Product getProduct(Payment payment) {
        ProductCreateParams productParams = new ProductCreateParams.Builder()
                .setName(getCarName(payment))
                .setDefaultPriceData(ProductCreateParams.DefaultPriceData.builder()
                        .setUnitAmount(countTotal(payment) * PRICE_MULTIPLIER)
                        .setCurrency(CURRENCY_NAME)
                        .build())
                .build();
        Product product = new Product();
        try {
            product = Product.create(productParams);
        } catch (StripeException e) {
            throw new PaymentException("Product " + product.getId() + " creation went wrong!");
        }
        return product;
    }

    private void checkRentalDates(Rental rental) {
        if (rental.getReturnDate() == null) {
            throw new PaymentException("You can't carry out payment if return date is null!");
        }
    }

    private void existsPendingPayment() {
        boolean check = paymentRepository.findAll().stream()
                .anyMatch(payment -> payment.getStatus().equals(Payment.Status.PENDING));
        if (check) {
            throw new PaymentException(
                    "You have  unpaid payments! New payment can not be created!");
        }
    }

    private void existsPaymentForRental(Rental rental) {
        boolean check = paymentRepository.findAll().stream()
                .anyMatch(payment -> payment.getRental().equals(rental));
        if (check) {
            throw new PaymentException(
                    "You have already payment for this rental! New payment can not be created!");
        }
    }

    private String getCarName(Payment payment) {
        return payment.getRental().getCar().getModel();
    }

    private Long countTotal(Payment payment) {
        calculator = PaymentCalculationsHandler.getCalculator(payment);
        return calculator.calculateTotal(payment);
    }
}
