package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalByManagerDto;
import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalSearchParameters;
import com.jvavateam.carsharingapp.exception.EntityNotFoundException;
import com.jvavateam.carsharingapp.mapper.rental.RentalMapper;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
import com.jvavateam.carsharingapp.repository.rental.RentalSpecificationBuilder;
import com.jvavateam.carsharingapp.service.CarService;
import com.jvavateam.carsharingapp.service.RentalService;
import com.jvavateam.carsharingapp.service.UserService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final CarService carService;
    private final UserService userService;
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final RentalSpecificationBuilder rentalSpecificationBuilder;

    @Override
    @Transactional
    public RentalResponseDto createByManager(CreateRentalByManagerDto createRentalByManagerDto) {
        decreaseCarInventory(createRentalByManagerDto.carId());
        Rental rental = rentalMapper.toModel(createRentalByManagerDto);
        Rental savedRental = rentalRepository.save(rental);
        return rentalMapper.toDto(savedRental);
    }

    @Override
    @Transactional
    public RentalResponseDto create(CreateRentalDto createRentalDto) {
        decreaseCarInventory(createRentalDto.carId());
        Rental rental = rentalMapper.toModel(createRentalDto);
        User currentUser = userService.getAuthentificatedUser();
        rental.setUser(currentUser);
        Rental savedRental = rentalRepository.save(rental);
        return rentalMapper.toDto(savedRental);
    }

    @Override
    @Transactional
    public List<RentalResponseDto> getAllByManager(RentalSearchParameters searchParameters,
                                                   Pageable pageable) {
        Specification<Rental> searchSpecification =
                rentalSpecificationBuilder.build(searchParameters);

        return rentalRepository.findAll(searchSpecification, pageable).stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public List<RentalResponseDto> getAll(Pageable pageable) {
        return rentalRepository.findAll(pageable).stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    public RentalResponseDto getById(Long id) {
        Rental rental = rentalRepository.getByIdForCurrentUser(id)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find rental with id: " + id));
        return rentalMapper.toDto(rental);
    }

    @Override
    @Transactional
    public RentalReturnResponseDto completeRental(Long id) {
        Rental rental = rentalRepository.getByIdForCurrentUser(id)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find rental with id: " + id));
        Car returnedCar = rental.getCar();
        increaseCarInventory(returnedCar.getId());
        rental.setActualReturnDate(LocalDate.now());
        rental.setActive(false);
        Rental savedRental = rentalRepository.save(rental);
        return rentalMapper.toReturnDto(savedRental);
    }

    private void increaseCarInventory(Long increasingCarId) {
        Car carForIncreasing = carService.findById(increasingCarId);
        carForIncreasing.setInventory(carForIncreasing.getInventory() + 1);
        carService.update(carForIncreasing);
    }

    private void decreaseCarInventory(Long decreasingCarId) {
        Car carForDecreasing = carService.findById(decreasingCarId);
        carForDecreasing.setInventory(carForDecreasing.getInventory() - 1);
        carService.update(carForDecreasing);
    }
}
