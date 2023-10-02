package com.jvavateam.carsharingapp.dto.car;

import com.jvavateam.carsharingapp.model.Car;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CarDtoRequest(@NotNull String model,
                            @NotNull String brand,
                            @Positive int inventory,
                            @NotNull @Min(0) BigDecimal dailyFee,
                            @NotNull Car.Type type) {
}
