package com.jvavateam.carsharingapp.repository.rental;

import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.repository.SpecificationBooleanProvider;
import com.jvavateam.carsharingapp.repository.SpecificationLongProvider;
import com.jvavateam.carsharingapp.repository.SpecificationProviderManager;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RentalSpecificationProviderManager implements SpecificationProviderManager<Rental> {
    private final List<SpecificationBooleanProvider<Rental>> rentalSpecificationBooleanProviders;
    private final List<SpecificationLongProvider<Rental>> rentalSpecificationLongProviders;

    @Override
    public SpecificationBooleanProvider<Rental> getBooleanSpecificationProvider(String key) {
        return rentalSpecificationBooleanProviders.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Can`t find correct specification provider for key: "
                                        + key));
    }
    @Override
    public SpecificationLongProvider<Rental> getLongSpecificationProvider(String key) {
        return rentalSpecificationLongProviders.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Can`t find correct specification provider for key: "
                                        + key));
    }
}
