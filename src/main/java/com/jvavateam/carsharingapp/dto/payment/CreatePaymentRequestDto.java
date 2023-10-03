package com.jvavateam.carsharingapp.dto.payment;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record CreatePaymentRequestDto(
        @NotEmpty
        @Length(min = 10, max = 100)
        String rentalId,
        @NotEmpty String paymentType
) {
}
