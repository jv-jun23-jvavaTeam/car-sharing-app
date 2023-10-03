package com.jvavateam.carsharingapp.service;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalSearchParameters;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface RentalService {
    RentalResponseDto create(CreateRentalDto createRentalDto);

    List<RentalResponseDto> getAll(RentalSearchParameters parameters,
                                   Pageable pageable);

    RentalResponseDto getById(Long id);

    RentalReturnResponseDto completeRental(Long rentalId);
}
