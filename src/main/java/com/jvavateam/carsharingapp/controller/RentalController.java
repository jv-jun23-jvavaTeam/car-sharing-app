package com.jvavateam.carsharingapp.controller;

import com.jvavateam.carsharingapp.dto.rental.CreateRentalDto;
import com.jvavateam.carsharingapp.dto.rental.RentalResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalReturnResponseDto;
import com.jvavateam.carsharingapp.dto.rental.RentalSearchParameters;
import com.jvavateam.carsharingapp.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/rentals")
@Tag(name = "Orders management",
        description = "Endpoints for managing user orders")
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place new rental",
            description = "Add a new rental (decrease car inventory by 1)")
    public RentalResponseDto create(@Valid @RequestBody CreateRentalDto createRentalDto) {
        return rentalService.create(createRentalDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all user rentals",
            description = "Get rentals by user ID and whether the rental is still active or not")
    public List<RentalResponseDto> getAll(@ModelAttribute RentalSearchParameters parameters, Pageable pageable) {
        return rentalService.getAll(parameters, pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user rental by id",
            description = "Get specific rental")
    public RentalResponseDto get(@PathVariable Long id) {
        return rentalService.getById(id);
    }

    @PostMapping("/rentals/{id}/return")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update rental return date",
            description = "Set actual return date (increase car inventory by 1)")
    public RentalReturnResponseDto updateReturnDate(@PathVariable Long id) {
        return rentalService.completeRental(id);
    }
}
