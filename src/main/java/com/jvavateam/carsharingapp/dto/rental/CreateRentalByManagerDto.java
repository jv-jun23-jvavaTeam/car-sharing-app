package com.jvavateam.carsharingapp.dto.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateRentalByManagerDto(
        @NotNull(message = "Rental date cannot be null")
        @FutureOrPresent
        @Schema(description = "Rental date", example = "2023-10-03")
        LocalDate rentalDate,

        @NotNull(message = "Return date cannot be null")
        @Future
        @Schema(description = "Return date", example = "2023-10-18")
        LocalDate returnDate,

        @NotNull(message = "Car id cannot be null")
        @Schema(description = "Car ID", example = "1")
        Long carId,

        @NotNull(message = "User id cannot be null")
        @Schema(description = "User ID", example = "123")
        Long userId) {
}
