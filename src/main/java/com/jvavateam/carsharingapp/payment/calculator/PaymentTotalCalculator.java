package com.jvavateam.carsharingapp.payment.calculator;

import com.jvavateam.carsharingapp.model.Payment;

public interface PaymentTotalCalculator {
    Long calculateTotal(Payment payment);
}