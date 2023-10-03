package com.jvavateam.carsharingapp.mapper.car;

import com.jvavateam.carsharingapp.config.MapperConfiguration;
import com.jvavateam.carsharingapp.dto.car.CarDtoRequest;
import com.jvavateam.carsharingapp.dto.car.CarDtoResponse;
import com.jvavateam.carsharingapp.model.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class)
public interface CarMapper {
    CarDtoResponse toDto(Car car);

    @Mapping(target = "id", ignore = true)
    Car toEntity(CarDtoRequest carDto);
}
