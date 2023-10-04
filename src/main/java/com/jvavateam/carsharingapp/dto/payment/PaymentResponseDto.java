package com.jvavateam.carsharingapp.dto.payment;

import java.math.BigDecimal;

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
