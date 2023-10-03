package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.mapper.rental.RentalMapper;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.UserRepository;
import com.jvavateam.carsharingapp.repository.car.CarRepository;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
import com.jvavateam.carsharingapp.service.RentalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;

    @Override
    @Transactional
    public RentalResponseDto create(CreateRentalDto createRentalDto) {
        Car car = carRepository.findById(createRentalDto.carId())
                .orElseThrow(() -> new RuntimeException("Car with provided id not found"));
        if (car.getInventory() < 1) {
            throw new RuntimeException("Can`t create new rental: This car is absent");
        }
        User user = userRepository.findById(createRentalDto.userId())
                .orElseThrow(() -> new RuntimeException("User with provided id not found"));
        Rental rental = rentalMapper.toModel(createRentalDto);
        rental.setCar(car);
        rental.setUser(user);
        rental.setActive(true);
        Rental savedRental = rentalRepository.save(rental);
        //DECREASE CAR INVENTORY
        return rentalMapper.toDto(savedRental);
    }

    @Override
    public List<RentalResponseDto> getAll() {
        List<Rental> rentals = rentalRepository.getAllForCurrentUser();
        return rentals.stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    public RentalResponseDto getById(Long id) {
        Rental rental = rentalRepository.getByIdForCurrentUser(id)
                .orElseThrow(() -> new RuntimeException("Can`t find rental with id: " + id));
        return rentalMapper.toDto(rental);
    }

    @Override
    @Transactional
    public RentalReturnResponseDto completeRental(Long id) {
        Rental rental = rentalRepository.getByIdForCurrentUser(id)
                .orElseThrow(() -> new RuntimeException("Can`t find rental with id: " + id));
        //INCREASE CAR INVENTORY
        rental.setActive(false);
        Rental savedRental = rentalRepository.save(rental);
        return rentalMapper.toReturnDto(savedRental);
    }
}
