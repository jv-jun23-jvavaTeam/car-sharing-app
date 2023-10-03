package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.mapper.rental.RentalMapper;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.car.CarRepository;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
import com.jvavateam.carsharingapp.repository.user.UserRepository;
import com.jvavateam.carsharingapp.service.RentalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;

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
        List<Rental> rentals = rentalRepository.getAllForCurrentUser();
        List<RentalResponseDto> rentalsDto = rentals.stream()
                .map(rental -> rentalMapper.toDto(rental))
                .toList();
        return null;
    }

    @Override
    public CreateRentalResponseDto getById(Long id) {
        return null;
    }

    @Override
    public RentalResponseDto completeRental() {
        return null;
    }
}
