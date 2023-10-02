package com.jvavateam.carsharingapp.repository;

import com.jvavateam.carsharingapp.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
}
