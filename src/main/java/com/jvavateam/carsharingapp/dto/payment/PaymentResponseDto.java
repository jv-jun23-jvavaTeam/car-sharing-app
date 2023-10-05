package com.jvavateam.carsharingapp.dto.payment;

import java.math.BigDecimal;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public record PaymentResponseDto(
        Long id,
        String status,
        String type,
        String sessionUrl,
        String sessionId,
        Long rentalId,
        BigDecimal amountToPay
) {
}
