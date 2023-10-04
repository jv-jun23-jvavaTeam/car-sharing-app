package com.jvavateam.carsharingapp.service.payment.calculator;

import com.jvavateam.carsharingapp.model.Payment;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class PaymentTotalCalculatorForPayment implements PaymentTotalCalculator {
    @Override
    public Long calculateTotal(Payment payment) {
        long daysToRent = ChronoUnit.DAYS.between(payment.getRental().getRentalDate(),
                payment.getRental().getReturnDate());
        BigDecimal total = payment.getRental().getCar().getDailyFee()
                .multiply(BigDecimal.valueOf(daysToRent));
        return total.longValue();
    }
}
