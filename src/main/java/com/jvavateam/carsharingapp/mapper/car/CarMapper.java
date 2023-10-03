package com.jvavateam.carsharingapp.mapper.car;

import com.jvavateam.carsharingapp.config.MapperConfiguration;
import com.jvavateam.carsharingapp.dto.car.CarDtoRequest;
import com.jvavateam.carsharingapp.dto.car.CarDtoResponse;
import com.jvavateam.carsharingapp.model.Car;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface CarMapper {
    CarDtoResponse toDto(Car car);

    Car toEntity(CarDtoRequest carDto);
}
