package com.jvavateam.carsharingapp.controller;

import com.jvavateam.carsharingapp.dto.payment.CreatePaymentRequestDto;
import com.jvavateam.carsharingapp.dto.payment.PaymentOperationMessage;
import com.jvavateam.carsharingapp.dto.payment.PaymentResponseDto;
import com.jvavateam.carsharingapp.payment.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Payments management",
        description = "Endpoints for managing payments")
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Create new payment session",
            description = "Create new payment session")
    public PaymentResponseDto createPayment(@RequestBody CreatePaymentRequestDto requestDto) {
        return paymentService.create(requestDto);
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
        return paymentService.getAllPaused();
    }

    @GetMapping("/successful")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Retrieving all successful payments",
            description = "Retrieving all successful payments for current user")
    public List<PaymentResponseDto> getAllPausedPayments() {
        return paymentService.getAllSuccessful();
    }

    @GetMapping("/success")
    public PaymentOperationMessage successful(@RequestParam String sessionId) {
        paymentService.updateStatus(sessionId);
        return new PaymentOperationMessage("Payment " + sessionId + " successfully provided!");
    }

    @GetMapping("/cancel")
    public PaymentOperationMessage error() {
        return new PaymentOperationMessage(
                "Payment cancelled! You can try again later. "
                + "The session will be open for 24hours."
        );
    }
}
