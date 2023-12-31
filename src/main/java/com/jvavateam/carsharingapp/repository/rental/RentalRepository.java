package com.jvavateam.carsharingapp.repository.rental;

import com.jvavateam.carsharingapp.model.Rental;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface RentalRepository extends JpaRepository<Rental, Long>,
        JpaSpecificationExecutor<Rental> {

    @EntityGraph(attributePaths = {"car", "user"})
    @Query("""
            FROM Rental r
            WHERE r.id = :id
            AND r.user.id = ?#{principal?.id}
            """)
    Optional<Rental> findByIdForCurrentUser(Long id);

    @EntityGraph(attributePaths = {"car", "user"})
    @Query("""
            FROM Rental r
            WHERE r.user.id = ?#{principal?.id}
            """)
    List<Rental> findAllForCurrentUser(Pageable pageable);

    @EntityGraph(attributePaths = {"car", "user"})
    @Query("""
                FROM Rental r
                WHERE r.returnDate > :tomorrow
            """)
    List<Rental> findAllOverdue(LocalDate tomorrow);
}
