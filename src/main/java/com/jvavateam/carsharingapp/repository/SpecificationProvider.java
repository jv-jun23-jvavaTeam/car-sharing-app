package com.jvavateam.carsharingapp.repository;

import com.jvavateam.carsharingapp.model.Rental;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<Rental> getSpecification(Boolean params);

    Specification<Rental> getSpecification(Long params);
}
