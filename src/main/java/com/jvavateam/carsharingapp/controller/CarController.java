package com.jvavateam.carsharingapp.controller;

import com.jvavateam.carsharingapp.dto.car.CarDtoRequest;
import com.jvavateam.carsharingapp.dto.car.CarDtoResponse;
import com.jvavateam.carsharingapp.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cars")
@Tag(name = "Cars management",
        description = "Endpoints for managing cars")
public class CarController {
    private final CarService carService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Create a car",
            description = "Create and save a new car in database")
    public CarDtoResponse create(@Valid @RequestBody CarDtoRequest carDto) {
        return carService.create(carDto);
    }

    @GetMapping
    @Operation(summary = "Get car by id",
            description = "Get certain car by id")
    public CarDtoResponse get(@PathVariable Long id) {
        return carService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Get all cars")
    public List<CarDtoResponse> getAll() {
        return carService.getAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a car info",
            description = "Update a car info by id")
    public CarDtoResponse update(@RequestBody CarDtoRequest carDto,
                                 @PathVariable Long id) {
        return carService.update(carDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a car",
            description = "Delete a car by id")
    public void delete(@PathVariable Long id) {
        carService.deleteById(id);
    }
}
