package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.service.RentalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    @Override
    public CreateRentalResponseDto create(CreateRentalDto createRentalDto) {
        return null;
    }

    @Override
    public List<CreateRentalResponseDto> getAll() {
        return null;
    }

    @Override
    public CreateRentalResponseDto getById(Long id) {
        return null;
    }

    @Override
    public RentalReturnResponseDto completeRental() {
        return null;
    }
}
