package com.jvavateam.carsharingapp.service.payment;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.mapper.payment.PaymentMapper;
import com.jvavateam.carsharingapp.model.Payment;
import com.jvavateam.carsharingapp.repository.payment.PaymentRepository;
import com.jvavateam.carsharingapp.service.payment.calculator.PaymentCalculationsHandler;
import com.jvavateam.carsharingapp.service.payment.calculator.PaymentTotalCalculator;
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

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String CURRENCY_NAME = "usd";
    private static final Long MAX_NUMBER_OF_CARS_TO_RENT = 1L;
    private static final Long EXPIRATION_TIME = Instant.now().getEpochSecond() + 86400L;

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private PaymentTotalCalculator calculator;

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
    public PaymentResponseDto createPayment(CreatePaymentRequestDto requestDto)
            throws StripeException {
        Stripe.apiKey = stripeKey;

        Payment payment = paymentMapper.toEntity(requestDto);

        Session session = createSession(payment);

        payment.setSessionUrl(session.getUrl());
        payment.setSessionId(session.getId());
        payment.setAmountToPay(BigDecimal.valueOf(500L));
        //paymentRepository.save(payment);
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
        Payment payment = paymentRepository.findBySessionId(sessionId);
        payment.setStatus(Payment.Status.PAID);
        paymentRepository.save(payment);
    }

    private Session createSession(Payment payment)
            throws StripeException {
        Product product = getProduct(getCarName(payment));
        Price price = getPrice(product);
        return getSession(price);
    }

    private Session getSession(Price price) throws StripeException {
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
        Session session = Session.create(sessionCreateParams);
        return session;
    }

    private static Price getPrice(Product product) throws StripeException {
        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setCurrency(CURRENCY_NAME)
                .setProduct(product.getId())
                .setUnitAmount(500L)
                .build();
        Price price = Price.create(priceParams);
        return price;
    }

    private Product getProduct(String carName) throws StripeException {
        ProductCreateParams productParams = new ProductCreateParams.Builder()
                .setName(carName)
                .build();
        
        return Product.create(productParams);
    }

    private String getCarName(Payment payment) {
        return payment.getRental().getCar().getBrand()
                + " " + payment.getRental().getCar().getModel();
    }

    private Long countTotal(Payment payment) {
        calculator = PaymentCalculationsHandler.getCalculator(payment);
        return calculator.calculateTotal(payment);
    }
}
