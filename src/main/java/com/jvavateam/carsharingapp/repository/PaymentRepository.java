package com.jvavateam.carsharingapp.repository;

import com.jvavateam.carsharingapp.model.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    //@Query("FROM Payment p join fetch Rental r WHERE r.user.id = ?#{ principal?.id}"
    // "AND p.status = :status")
    List<Payment> findAllByStatusForCurrentUser(Payment.Status status);

    //There will be added user filtering after security is added
    @Query("FROM Payment p join fetch Rental r")
    List<Payment> findAllForCurrentUser();
}
