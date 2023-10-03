package com.jvavateam.carsharingapp.service.impl;

import com.jvavateam.carsharingapp.dto.car.CarDtoRequest;
import com.jvavateam.carsharingapp.dto.car.CarDtoResponse;
import com.jvavateam.carsharingapp.repository.car.CarRepository;
import com.jvavateam.carsharingapp.service.CarService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;

    @Override
    public CarDtoResponse create(CarDtoRequest carDto) {
        return null;
    }

    @Override
    public CarDtoResponse getById(Long id) {
        return null;
    }

    @Override
    public CarDtoResponse update(CarDtoRequest carDto, Long id) {
        return null;
    }

    @Override
    public List<CarDtoResponse> getAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
