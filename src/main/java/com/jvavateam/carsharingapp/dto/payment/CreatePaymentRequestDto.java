package com.jvavateam.carsharingapp.dto.payment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreatePaymentRequestDto(
        @NotNull(message = "Rental ID can not be null")
        @Positive(message = "Rental ID must be not negative")
        @Schema(description = "Rental ID", example = "1")
        Long rentalId,
        @NotEmpty(message = "Payment type can not be empty")
        @Schema(description = "Payment type", example = "PAYMENT")
        String paymentType
) {
}
