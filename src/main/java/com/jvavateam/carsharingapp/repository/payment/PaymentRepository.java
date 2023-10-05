package com.jvavateam.carsharingapp.repository.payment;

import com.jvavateam.carsharingapp.model.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("""
            FROM Payment p JOIN FETCH p.rental r
            WHERE r.user.id = ?#{ principal?.id}
            AND p.status = :status")
            """)
    List<Payment> findAllByStatus(Payment.Status status);

    @Query("""
            FROM Payment p JOIN FETCH p.rental r
            WHERE r.user.id = ?#{principal?.id}
            """)
    List<Payment> findAll();

    @EntityGraph(attributePaths = {"rental"})
    List<Payment> findAllByRentalUserId(Long id);

    Optional<Payment> findBySessionId(String sessionId);
}
