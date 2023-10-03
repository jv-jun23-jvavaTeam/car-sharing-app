package com.jvavateam.carsharingapp.dto.rental;

import java.time.LocalDate;

public record CreateRentalResponseDto(Long id,
                                      LocalDate rentalDate,
                                      LocalDate returnDate,
                                      Long carId,
                                      Long userId) {
}
