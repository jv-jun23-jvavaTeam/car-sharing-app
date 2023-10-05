package com.jvavateam.carsharingapp.payment.calculator;

import com.jvavateam.carsharingapp.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentCalculationsHandler {
    public static TotalCalculator getCalculator(Payment payment) {
        if (payment.getType() == Payment.Type.PAYMENT) {
            return new PaymentCalculator();
        }
        return new FeeCalculator();
    }
}
