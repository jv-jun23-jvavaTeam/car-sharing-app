package com.jvavateam.carsharingapp.dto.rental;

import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public record RentalResponseDto(Long id, LocalDate rentalDate, LocalDate returnDate, LocalDate actualReturnDate,
                                Long carId, Long userId) {
}
