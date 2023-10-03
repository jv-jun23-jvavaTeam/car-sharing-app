package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.mapper.RentalMapper;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.CarRepository;
import com.jvavateam.carsharingapp.repository.UserRepository;
import com.jvavateam.carsharingapp.service.RentalService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalMapper rentalMapper;

    @Override
    public CreateRentalResponseDto create(CreateRentalDto createRentalDto) {
        Rental rental = rentalMapper.toModel(createRentalDto);

        Optional<Car> possibleCar = carRepository.findById(createRentalDto.carId());
        // or from userService
        Optional<User> possibleUser = userRepository.findById(createRentalDto.userId());
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
