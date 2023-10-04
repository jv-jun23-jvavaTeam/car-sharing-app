package com.jvavateam.carsharingapp.payment.calculator;

import com.jvavateam.carsharingapp.model.Payment;

public interface TotalCalculator {
    Long calculateTotal(Payment payment);
}
