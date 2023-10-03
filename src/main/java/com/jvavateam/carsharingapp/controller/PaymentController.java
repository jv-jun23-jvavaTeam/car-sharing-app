package com.jvavateam.carsharingapp.controller;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentOperationMessage;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.service.payment.PaymentService;
import com.stripe.exception.StripeException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponseDto createPayment(CreatePaymentRequestDto requestDto)
            throws StripeException {
        return paymentService.createPayment(requestDto);
    }

    @GetMapping
    public List<PaymentResponseDto> getAll() {
        return paymentService.getAllForCurrentUser();
    }

    @GetMapping("/paused")
    public List<PaymentResponseDto> getAllSuccessfulPayments() {
        return paymentService.getAllSuccessfulPayments();
    }

    @GetMapping("/successful")
    public List<PaymentResponseDto> getAllPausedPayments() {
        return paymentService.getAllPausedPayments();
    }

    @GetMapping("/success")
    public PaymentOperationMessage successful(CreatePaymentRequestDto requestDto) {
        return new PaymentOperationMessage("Succesfull!");
    }

    @GetMapping("/cancel")
    public PaymentOperationMessage error(CreatePaymentRequestDto requestDto) {
        return new PaymentOperationMessage("Error!");
    }
}
