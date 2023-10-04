package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.car.CarDtoRequest;
import com.jvavateam.carsharingapp.dto.car.CarDtoResponse;
import com.jvavateam.carsharingapp.exception.EntityNotFoundException;
import com.jvavateam.carsharingapp.mapper.car.CarMapper;
import com.jvavateam.carsharingapp.model.Car;
import com.jvavateam.carsharingapp.repository.car.CarRepository;
import com.jvavateam.carsharingapp.service.CarService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDtoResponse create(CarDtoRequest carDto) {
        return carMapper.toDto(carRepository.save(carMapper.toEntity(carDto)));
    }

    @Override
    public CarDtoResponse getById(Long id) {
        return carMapper.toDto(findById(id)
        );
    }

    @Override
    public CarDtoResponse update(CarDtoRequest carDto, Long id) {
        Car car = carMapper.toEntity(carDto);
        car.setId(id);
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public Car update(Car car) {
        return carRepository.save(car);
    }

    @Override
    public List<CarDtoResponse> getAll() {
        return carRepository.findAll().stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public void updateInventory(Car car) {
        Car updatedCar = carRepository.findById(car.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find car by id: " + car.getId())
        );
        updatedCar.setInventory(car.getInventory());
        carRepository.save(updatedCar);
    }

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find car by id: " + id)
        );
    }
}
