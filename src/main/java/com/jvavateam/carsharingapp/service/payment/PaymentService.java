package com.jvavateam.carsharingapp.service.payment;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;

public interface PaymentService {
    String getAllForCurrentUser();

    String createPayment(CreatePaymentRequestDto requestDto);

    String getAllSuccessfulPayments();

    String getAllPausedPayments();
}