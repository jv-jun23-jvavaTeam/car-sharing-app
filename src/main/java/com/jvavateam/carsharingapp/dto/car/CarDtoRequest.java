package com.jvavateam.carsharingapp.dto.car;

import com.jvavateam.carsharingapp.model.Car;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Value
public record CarDtoRequest(String model, String brand, int inventory,
                            BigDecimal dailyFee, Car.Type type) {
}
