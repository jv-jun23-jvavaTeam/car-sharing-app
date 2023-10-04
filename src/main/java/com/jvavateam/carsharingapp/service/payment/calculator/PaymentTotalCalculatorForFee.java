package com.jvavateam.carsharingapp.service.payment.calculator;

import com.jvavateam.carsharingapp.model.Payment;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class PaymentTotalCalculatorForFee implements PaymentTotalCalculator {
    private static BigDecimal FEE_PERCENTAGE = BigDecimal.valueOf(10);

    @Override
    public Long calculateTotal(Payment payment) {
        long overdueDays = ChronoUnit.DAYS.between(payment.getRental().getActualReturnDate(),
                payment.getRental().getReturnDate());
        BigDecimal total = payment.getRental().getCar().getDailyFee()
                .multiply(FEE_PERCENTAGE)
                .multiply(BigDecimal.valueOf(overdueDays));
        return total.longValue();
    }
}
