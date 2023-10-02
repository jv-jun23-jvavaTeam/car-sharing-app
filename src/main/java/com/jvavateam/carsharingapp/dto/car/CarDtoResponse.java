package com.jvavateam.carsharingapp.dto.car;

import com.jvavateam.carsharingapp.model.Car;
import java.math.BigDecimal;

public record CarDtoResponse(Long id, String model, String brand, Car.Type type,
                             int inventory, BigDecimal dailyFee) {
}
