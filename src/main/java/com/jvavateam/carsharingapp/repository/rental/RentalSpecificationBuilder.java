package com.jvavateam.carsharingapp.repository.rental;

import com.jvavateam.carsharingapp.dto.rental.RentalSearchParameters;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.repository.SpecificationBuilder;
import com.jvavateam.carsharingapp.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RentalSpecificationBuilder implements SpecificationBuilder<Rental> {
    private final SpecificationProviderManager<Rental> rentalSpecificationProviderManager;

    @Override
    public Specification<Rental> build(RentalSearchParameters searchParameters) {
        Specification<Rental> spec = Specification.where(null);
        if (searchParameters.isActive() != null) {
            spec = spec.and(rentalSpecificationProviderManager.getBooleanSpecificationProvider("is_active")
                    .getSpecification(searchParameters.isActive()));
        }
        if (searchParameters.userId() != null) {
            spec = spec.and(rentalSpecificationProviderManager.getLongSpecificationProvider("user_id")
                    .getSpecification(searchParameters.userId()));
        }
        return spec;
    }
}
