package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.service.RentalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    @Override
    public RentalResponseDto create(CreateRentalDto createRentalDto) {
        return null;
    }

    @Override
    public List<RentalResponseDto> getAll() {
        return null;
    }

    @Override
    public RentalResponseDto getById(Long id) {
        return null;
    }

    @Override
    public RentalResponseDto completeRental() {
        return null;
    }
}
