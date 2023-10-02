package com.jvavateam.carsharingapp.dto.rental;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateRentalDto(@NotNull(message = "Rental date cannot be null")
                              @FutureOrPresent LocalDate rentalDate,
                              @NotNull(message = "Car id cannot be null") Long carId,
                              @NotNull(message = "User id cannot be null") Long userId) {
}
