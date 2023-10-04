package com.jvavateam.carsharingapp.dto.rental;

import jakarta.validation.constraints.NotNull;

public record RentalSearchParameters(@NotNull(message = "User Id cannot be empty")
                                     Long userId,
                                     @NotNull(message = "isActive cannot be empty")
                                     Boolean isActive) {
}
