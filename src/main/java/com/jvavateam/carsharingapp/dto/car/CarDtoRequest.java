package com.jvavateam.carsharingapp.dto.car;

import com.jvavateam.carsharingapp.model.Car;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CarDtoRequest(
        @NotNull(message = "Car brand cannot be null")
        @Schema(description = "Car brand", example = "BMW")
        String brand,
        @NotNull(message = "Car model cannot be null")
        @Schema(description = "Car model", example = "X5")
        String model,
        @Positive(message = "Inventory must be positive")
        @Schema(description = "Quantity of available cars", example = "1")
        int inventory,
        @NotNull(message = "Daily fee cannot be null")
        @Schema(description = "Daily fee of a car", example = "49.99")
        @Min(0)
        BigDecimal dailyFee,
        @NotNull(message = "Car type cannot be null")
        @Schema(description = "Car body type", example = "SUV")
        Car.Type type) {
}
