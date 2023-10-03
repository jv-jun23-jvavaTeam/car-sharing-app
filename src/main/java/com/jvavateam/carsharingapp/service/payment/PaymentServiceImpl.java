package com.jvavateam.carsharingapp.service.payment;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.repository.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private PaymentRepository paymentRepository;

    @Override
    public String getAllForCurrentUser() {
        return "***All payments for current user***";
    }

    @Override
    public String createPayment(CreatePaymentRequestDto requestDto) {
        return "***Payment successfully created***";
    }

    @Override
    public String getAllSuccessfulPayments() {
        return "***Successful payment redirection***";
    }

    @Override
    public String getAllPausedPayments() {
        return "***Paused payment redirection***";
    }
}
