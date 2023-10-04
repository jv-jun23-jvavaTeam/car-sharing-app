package com.jvavateam.carsharingapp.controller;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentOperationMessage;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.service.payment.PaymentService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Create new payment session",
            description = "Create new payment session")
    public PaymentResponseDto createPayment(@RequestBody CreatePaymentRequestDto requestDto)
            throws StripeException {
        return paymentService.createPayment(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Retrieving all payments",
            description = "Retrieving all payments for current user")
    public List<PaymentResponseDto> getAll() {
        return paymentService.getAllForCurrentUser();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "Retrieving all payments by user id",
            description = "Retrieving all payments for certain user")
    public List<PaymentResponseDto> getAllForUser(@PathVariable Long id) {
        return paymentService.getAllForUser(id);
    }

    @GetMapping("/paused")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Retrieving all paused payments",
            description = "Retrieving all paused payments for current user")
    public List<PaymentResponseDto> getAllSuccessfulPayments() {
        return paymentService.getAllPausedPayments();
    }

    @GetMapping("/successful")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Retrieving all successful payments",
            description = "Retrieving all successful payments for current user")
    public List<PaymentResponseDto> getAllPausedPayments() {
        return paymentService.getAllSuccessfulPayments();
    }

    @GetMapping("/success/")
    public PaymentOperationMessage successful(@RequestParam String sessionId) {
        paymentService.updatePaymentStatus(sessionId);
        return new PaymentOperationMessage("Payment " + sessionId + " successfully provided!");
    }

    @GetMapping("/cancel")
    public PaymentOperationMessage error() {
        return new PaymentOperationMessage("Payment cancelled! You can try again later. "
                + "The session will be open for 24hours.");
    }
}
