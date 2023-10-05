package com.jvavateam.carsharingapp.payment;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.stripe.exception.StripeException;
import java.util.List;

public interface PaymentService {
    List<PaymentResponseDto> getAllForCurrentUser();

    List<PaymentResponseDto> getAllForUser(Long id);

    PaymentResponseDto create(CreatePaymentRequestDto requestDto);

    List<PaymentResponseDto> getAllSuccessfulPayments();

    List<PaymentResponseDto> getAllPausedPayments();

    void updatePaymentStatus(String sessionId);
}