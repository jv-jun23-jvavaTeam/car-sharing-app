package com.jvavateam.carsharingapp.dto.rental;

import java.time.LocalDate;

public record RentalReturnResponseDto(
        Long id,
        LocalDate rentalDate,
        LocalDate returnDate,
        LocalDate actualReturnDate,
        Long carId,
        Long userId,
        boolean isActive) {
}
