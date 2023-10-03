package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.mapper.rental.RentalMapper;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
import com.jvavateam.carsharingapp.repository.user.UserRepository;
import com.jvavateam.carsharingapp.repository.car.CarRepository;
import com.jvavateam.carsharingapp.service.RentalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private CarRepository carRepository;
    private UserRepository userRepository;
    private RentalRepository rentalRepository;
    private RentalMapper rentalMapper;

    @Override
    public CreateRentalResponseDto create(CreateRentalDto createRentalDto) {
        Car car = carRepository.findById(createRentalDto.carId())
                .orElseThrow(() -> new RuntimeException("Car with provided id not found"));
        User user = userRepository.findById(createRentalDto.userId())
                .orElseThrow(() -> new RuntimeException("User with provided id not found"));
        Rental rental = rentalMapper.toModel(createRentalDto);
        rental.setCar(car);
        rental.setUser(user);
        Rental savedRental = rentalRepository.save(rental);
        return rentalMapper.toCreateDto(savedRental);
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
