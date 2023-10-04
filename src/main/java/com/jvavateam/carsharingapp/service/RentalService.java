package com.jvavateam.carsharingapp.service;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalByManagerDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalSearchParameters;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface RentalService {
    RentalResponseDto createByManager(CreateRentalByManagerDto createRentalByManagerDto);

    RentalResponseDto create(CreateRentalDto createRentalByManagerDto);

    List<RentalResponseDto> getAllByManager(RentalSearchParameters parameters,
                                            Pageable pageable);

    List<RentalResponseDto> getAll(Pageable pageable);

    RentalResponseDto getById(Long id);

    RentalReturnResponseDto completeRental(Long rentalId);
}
