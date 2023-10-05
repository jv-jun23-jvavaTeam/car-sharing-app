package com.jvavateam.carsharingapp.repository.rental;

import com.jvavateam.carsharingapp.model.Rental;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    Optional<Rental> getByIdForCurrentUser(Long id);

    @EntityGraph
    @Query("""
                FROM Rental r
                WHERE r.rentalDate > :tomorrow
            """)
    List<Rental> findAllOverdue(LocalDate tomorrow);
}
