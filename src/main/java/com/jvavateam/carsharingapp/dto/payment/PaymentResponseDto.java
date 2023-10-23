package com.jvavateam.carsharingapp.dto.payment;

import com.jvavateam.carsharingapp.model.Payment;
import java.math.BigDecimal;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public record PaymentResponseDto(
        Long id,
        Payment.Status status,
        Payment.Type type,
        String sessionUrl,
        String sessionId,
        Long rentalId,
        BigDecimal amountToPay
) {
}
