package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalSearchParameters;
import com.jvavateam.carsharingapp.exception.EntityNotFoundException;
import com.jvavateam.carsharingapp.exception.InvalidRequestParametersException;
import com.jvavateam.carsharingapp.mapper.rental.RentalMapper;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.model.Rental;
import com.jvavateam.carsharingapp.model.Role;
import com.jvavateam.carsharingapp.model.User;
import com.jvavateam.carsharingapp.repository.car.CarRepository;
import com.jvavateam.carsharingapp.repository.rental.RentalRepository;
import com.jvavateam.carsharingapp.repository.rental.RentalSpecificationBuilder;
import com.jvavateam.carsharingapp.repository.user.UserRepository;
import com.jvavateam.carsharingapp.service.RentalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final RentalSpecificationBuilder rentalSpecificationBuilder;

    @Override
    @Transactional
    public RentalResponseDto create(CreateRentalDto createRentalDto) {
        User user = userRepository.findById(createRentalDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User with provided id not found"));
        checkUserAccess(user);
        Car car = decreaseCarInventory(createRentalDto.carId());
        Rental rental = buildRental(createRentalDto, car, user);
        Rental savedRental = rentalRepository.save(rental);
        return rentalMapper.toDto(savedRental);
    }

    @Override
    @Transactional
    public List<RentalResponseDto> getAll(RentalSearchParameters searchParameters,
                                          Pageable pageable) {
        User currentUser = userRepository.getCurrentUser();
        if (!isManager(currentUser)) {
            checkUserAccess(currentUser);
        }
        Specification<Rental> searchSpecification =
                rentalSpecificationBuilder.build(searchParameters);

        return rentalRepository.findAll(searchSpecification, pageable).stream()
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
        increaseCarInventory(returnedCar);
        rental.setActive(false);
        Rental savedRental = rentalRepository.save(rental);
        return rentalMapper.toReturnDto(savedRental);
    }

    private void increaseCarInventory(Car carForIncreasing) {
        Long decreasingCarId = carForIncreasing.getId();
        Car actualCar = carRepository.findById(decreasingCarId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can`t find car with id: " + decreasingCarId));
        carForIncreasing.setInventory(actualCar.getInventory() + 1);
        carRepository.save(carForIncreasing);
    }

    private Car decreaseCarInventory(Long carIdForDecreasing) {
        Car actualCar = carRepository.findById(carIdForDecreasing)
                .orElseThrow(() -> new EntityNotFoundException("Car with provided id not found"));
        checkCarInventory(actualCar);
        actualCar.setInventory(actualCar.getInventory() - 1);
        return carRepository.save(actualCar);
    }

    private Rental buildRental(CreateRentalDto createRentalDto,
                               Car actualCar,
                               User user) {
        Rental rental = rentalMapper.toModel(createRentalDto);
        rental.setCar(actualCar);
        rental.setUser(user);
        rental.setActive(true);
        return rental;
    }

    private boolean isManager(User user) {
        return user.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals(Role.RoleName.MANAGER));
    }

    private void checkUserAccess(User user) {
        Long currentUserId = userRepository.getCurrentUser().getId();
        if (!user.getId().equals(currentUserId)) {
            throw new InvalidRequestParametersException("Wrong user id entered: " + user.getId());
        }
    }

    private void checkCarInventory(Car actualCar) {
        if (actualCar.getInventory() < 1) {
            throw new EntityNotFoundException("Can`t create new rental: This car is absent");
        }
    }
}
