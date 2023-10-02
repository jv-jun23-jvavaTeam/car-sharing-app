package com.jvavateam.carsharingapp.mapper.car;

import com.jvavateam.carsharingapp.dto.car.CarDtoRequest;
import com.jvavateam.carsharingapp.dto.car.CarDtoResponse;
import com.jvavateam.carsharingapp.model.Car;

public interface CarMapper {
    CarDtoResponse toDto(Car car);

    Car toEntity(CarDtoRequest carDto);
}
