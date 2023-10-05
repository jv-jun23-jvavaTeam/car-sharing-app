package com.jvavateam.carsharingapp.dto.car;

import com.jvavateam.carsharingapp.model.Car;
import java.math.BigDecimal;

public record CarDtoResponse(Long id,
                             String brand,
                             String model,
                             int inventory,
                             BigDecimal dailyFee,
                             Car.Type type) {
}
