package com.jvavateam.carsharingapp.dto.rental;

import java.time.LocalDate;

public record CreateRentalDto(LocalDate rentalDate, Long carId, Long userId) {
}
