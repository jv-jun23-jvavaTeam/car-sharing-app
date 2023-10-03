package com.jvavateam.carsharingapp.repository.rental;

import com.jvavateam.carsharingapp.model.Rental;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalRepository extends JpaRepository<Rental, Long>, JpaSpecificationExecutor<Rental> {
    @Query("FROM Rental r "
            + "WHERE r.user.id = ?#{ principal?.id}")
    List<Rental> findAllForCurrentUser();

    @Query("FROM Rental r "
            + "JOIN FETCH Car JOIN FETCH User "
            + "WHERE r.id = :id AND r.user.id = ?#{ principal?.id}")
    Optional<Rental> getByIdForCurrentUser(@Param("id") Long id);
}
