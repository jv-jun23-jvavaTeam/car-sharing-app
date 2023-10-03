package com.jvavateam.carsharingapp.dto.payment;

import java.math.BigDecimal;

public record PaymentResponseDto(
        String Status,
        BigDecimal amountPaid) {
}
