package com.jvavateam.carsharingapp.dto.car;

import com.jvavateam.carsharingapp.model.Car;
import java.math.BigDecimal;

public record CarDtoRequest(String model, String brand, int inventory,
                            BigDecimal dailyFee, Car.Type type) {
}
