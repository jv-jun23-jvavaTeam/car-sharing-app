package com.jvavateam.carsharingapp.service;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import java.util.List;

public interface RentalService {
    CreateRentalResponseDto create(CreateRentalDto createRentalDto);

    List<CreateRentalResponseDto> getAll();

    CreateRentalResponseDto getById(Long id);

    RentalReturnResponseDto completeRental();
}
