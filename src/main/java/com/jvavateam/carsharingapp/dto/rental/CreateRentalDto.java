package com.jvavateam.carsharingapp.dto.rental;

import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public record CreateRentalDto(LocalDate rentalDate, Long carId, Long userId) {
}
