package com.jvavateam.carsharingapp.service.payment;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.stripe.exception.StripeException;
import java.util.List;

public interface PaymentService {
    List<PaymentResponseDto> getAllForCurrentUser();

    PaymentResponseDto createPayment(CreatePaymentRequestDto requestDto) throws StripeException;

    List<PaymentResponseDto> getAllSuccessfulPayments();

    List<PaymentResponseDto> getAllPausedPayments();
}
