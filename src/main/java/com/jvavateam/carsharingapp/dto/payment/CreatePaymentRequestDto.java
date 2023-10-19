package com.jvavateam.carsharingapp.dto.payment;

import com.jvavateam.carsharingapp.model.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public record CreatePaymentRequestDto(
        @NotNull(message = "Rental ID can not be null")
        @Positive(message = "Rental ID must be not negative")
        @Schema(description = "Rental ID", example = "1")
        Long rentalId,
        @NotNull(message = "Payment type can not be null")
        @Schema(description = "Payment type", example = "PAYMENT")
        Payment.Type paymentType
) {
}
