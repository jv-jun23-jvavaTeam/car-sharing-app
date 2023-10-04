package com.jvavateam.carsharingapp.service.payment.calculator;

import com.jvavateam.carsharingapp.model.Payment;

public interface PaymentTotalCalculator {
    Long calculateTotal(Payment payment);
}
