package com.jvavateam.carsharingapp.repository.rental;

import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.repository.SpecificationProvider;
import com.jvavateam.carsharingapp.repository.SpecificationProviderManager;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RentalSpecificationProviderManager implements SpecificationProviderManager<Rental> {
    private final List<SpecificationProvider<Rental>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Rental> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Can`t find correct specification provider for key: "
                                        + key));
    }
}
