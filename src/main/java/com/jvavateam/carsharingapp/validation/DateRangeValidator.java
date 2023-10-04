package com.jvavateam.carsharingapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import org.springframework.beans.BeanWrapperImpl;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
    private static final String RENTAL_DATE_FIELD_NAME = "rentalDate";
    private static final String RETURN_DATE_FIELD_NAME = "returnDate";

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }
        LocalDate rentalDate = (LocalDate) new BeanWrapperImpl(dto)
                .getPropertyValue(RENTAL_DATE_FIELD_NAME);
        LocalDate returnDate = (LocalDate) new BeanWrapperImpl(dto)
                .getPropertyValue(RETURN_DATE_FIELD_NAME);
        return rentalDate.isBefore(returnDate);
    }
}
