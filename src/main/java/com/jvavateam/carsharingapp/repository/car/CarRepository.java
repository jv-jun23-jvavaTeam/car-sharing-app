package com.jvavateam.carsharingapp.repository.car;

import com.jvavateam.carsharingapp.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

}
