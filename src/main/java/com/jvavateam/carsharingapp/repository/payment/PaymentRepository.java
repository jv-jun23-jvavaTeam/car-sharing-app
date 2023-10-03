package com.jvavateam.carsharingapp.repository.payment;

import com.jvavateam.carsharingapp.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
