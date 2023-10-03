package com.jvavateam.carsharingapp.repository;

import com.jvavateam.carsharingapp.model.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    //@Query("FROM Payment p join fetch Rental r WHERE r.user.id = ?#{ principal?.id}"
    // "AND p.status = :status")
    List<Payment> findAllByStatus(Payment.Status status);

    // @Query("FROM Payment p join fetch Rental r WHERE r.user.id = ?#{principal?.id}")
    List<Payment> findAll();
}
