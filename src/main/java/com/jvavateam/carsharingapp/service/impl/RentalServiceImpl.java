package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
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


    @Override
    public RentalResponseDto create(CreateRentalDto createRentalDto) {
        Rental rental = new Rental();
        rental.setRentalDate(createRentalDto.rentalDate());
        Optional<Car> possibleCar = carRepository.findById(createRentalDto.carId());
        // or from userService
        Optional<User> possibleUser = userRepository.findById(createRentalDto.userId());
        rental.setCar();
        rental.setUser();
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
