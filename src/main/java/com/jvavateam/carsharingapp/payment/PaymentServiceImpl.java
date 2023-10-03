package com.jvavateam.carsharingapp.payment;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.mapper.PaymentMapper;
import com.jvavateam.carsharingapp.model.Payment;
import com.jvavateam.carsharingapp.payment.calculator.PaymentCalculationsHandler;
import com.jvavateam.carsharingapp.payment.calculator.PaymentTotalCalculator;
import com.jvavateam.carsharingapp.repository.PaymentRepository;
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
    private static final String SUCCESS_URL = "http://localhost:8080/api/payments/success";
    private static final String CANCEL_URL = "http://localhost:8080/api/payments/cancel";
    private static final Long MAX_NUMBER_OF_CARS_TO_RENT = 1L;
    private static final Long EXPIRATION_TIME = Instant.now().getEpochSecond() + 86400L;

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private PaymentTotalCalculator calculator;

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
        /*  payment.setRental(this.RENTAL);

        String carName = RENTAL.getCar().getBrand() + " " + RENTAL.getCar().getModel();*/

        Session session = createSession(payment, "newCar");

        payment.setSessionUrl(session.getUrl());
        payment.setSessionId(session.getId());
        payment.setAmountToPay(BigDecimal.valueOf(calculator.calculateTotal(payment)));
        //paymentRepository.save(payment);
        PaymentResponseDto responseDto = paymentMapper.toDto(payment);
        return responseDto;
    }

    private Session createSession(Payment payment, String carName)
            throws StripeException {
        ProductCreateParams productParams = new ProductCreateParams.Builder()
                .setName(carName)
                .build();
        Product product = Product.create(productParams);

        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setCurrency(CURRENCY_NAME)
                .setProduct(product.getId())
                .setUnitAmount(countTotal(payment))
                .build();
        Price price = Price.create(priceParams);

        SessionCreateParams sessionCreateParams = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(price.getId())
                        .setQuantity(MAX_NUMBER_OF_CARS_TO_RENT)
                        .build())
                .setExpiresAt(EXPIRATION_TIME)
                .setSuccessUrl(SUCCESS_URL)
                .setCancelUrl(CANCEL_URL)
                .build();

        Session session = Session.create(sessionCreateParams);
        return session;
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

    private Long countTotal(Payment payment) {
        calculator = PaymentCalculationsHandler.getCalculator(payment);
        return calculator.calculateTotal(payment);
    }
}
