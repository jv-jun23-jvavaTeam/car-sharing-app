package com.jvavateam.carsharingapp.service;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import java.util.List;

public interface RentalService {
    RentalResponseDto create(CreateRentalDto createRentalDto);

    List<RentalResponseDto> getAll();

    RentalResponseDto getById(Long id);

    RentalResponseDto completeRental();
}
