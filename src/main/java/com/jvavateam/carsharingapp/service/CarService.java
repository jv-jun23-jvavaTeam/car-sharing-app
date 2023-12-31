package com.jvavateam.carsharingapp.service;

import com.jvavateam.carsharingapp.dto.car.CarDtoRequest;
import com.jvavateam.carsharingapp.dto.car.CarDtoResponse;
import com.jvavateam.carsharingapp.model.Car;
import java.util.List;

public interface CarService {
    CarDtoResponse create(CarDtoRequest carDto);

    CarDtoResponse getById(Long id);

    CarDtoResponse update(CarDtoRequest carDto, Long id);

    Car update(Car car);

    List<CarDtoResponse> getAll();

    void deleteById(Long id);

    void updateInventory(Car car);

    Car findById(Long id);
}
