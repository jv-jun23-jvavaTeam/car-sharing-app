package com.jvavateam.carsharingapp.payment.calculator;

import com.jvavateam.carsharingapp.model.Payment;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class FeeCalculator implements TotalCalculator {
    private static BigDecimal FEE_PERCENTAGE = BigDecimal.valueOf(0.10);

    @Override
    public Long calculateTotal(Payment payment) {
        long overdueDays = ChronoUnit.DAYS.between(payment.getRental().getReturnDate(),
                payment.getRental().getActualReturnDate());
        BigDecimal total = payment.getRental().getCar().getDailyFee()
                .multiply(FEE_PERCENTAGE)
                .multiply(BigDecimal.valueOf(overdueDays));
        return total.longValue();
    }
}
