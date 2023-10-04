package com.jvavateam.carsharingapp.repository;

import com.jvavateam.carsharingapp.dto.rental.RentalSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(RentalSearchParameters searchParameters);
}
