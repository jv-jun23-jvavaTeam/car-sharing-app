package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.car.CarDtoRequest;
import com.jvavateam.carsharingapp.dto.car.CarDtoResponse;
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
        return carMapper.toDto(carRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find car by id: " + id)
        ));
    }

    @Override
    public CarDtoResponse update(CarDtoRequest carDto, Long id) {
        return carMapper.toDto(carRepository.save(carMapper.toEntity(carDto)));
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
    public void updateCarInventory(Car car) {
        Car updatedCar = carRepository.findById(car.getId()).orElseThrow(
                () -> new RuntimeException("Can't find car by id: " + car.getId())
        );
        updatedCar.setInventory(car.getInventory());
        carRepository.save(updatedCar);
    }
}
