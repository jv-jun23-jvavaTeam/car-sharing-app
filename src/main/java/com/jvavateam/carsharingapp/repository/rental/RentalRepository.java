package com.jvavateam.carsharingapp.repository.rental;

import com.jvavateam.carsharingapp.model.Rental;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("FROM Rental r WHERE r.user.id = ?#{ principal?.id}")
    List<Rental> getAllForCurrentUser();

    @Query("FROM Rental r WHERE r.id = :id AND r.user.id = ?#{ principal?.id}")
    Optional<Rental> getByIdForCurrentUser(@Param("id") Long id);
}
