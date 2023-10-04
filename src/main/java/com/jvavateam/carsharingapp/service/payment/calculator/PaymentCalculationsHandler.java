package com.jvavateam.carsharingapp.service.payment.calculator;

import com.jvavateam.carsharingapp.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentCalculationsHandler {
    public static PaymentTotalCalculator getCalculator(Payment payment) {
        if (payment.getType() == Payment.Type.PAYMENT) {
            return new PaymentTotalCalculatorForPayment();
        }
        return new PaymentTotalCalculatorForFee();
    }
}
