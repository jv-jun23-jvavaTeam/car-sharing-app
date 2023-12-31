package com.jvavateam.carsharingapp.dto.rental;

import com.jvavateam.carsharingapp.validation.DateRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@DateRange
public record CreateRentalDto(
        @NotNull(message = "Rental date cannot be null")
        @FutureOrPresent
        @Schema(description = "Rental date", example = "2023-10-03")
        LocalDate rentalDate,

        @NotNull(message = "Return date cannot be null")
        @Future(message = "Rental must be at least 1 day")
        @Schema(description = "Return date", example = "2023-10-18")
        LocalDate returnDate,

        @NotNull(message = "Car id cannot be null")
        @Schema(description = "Car ID", example = "1")
        Long carId) {
}
