package com.jvavateam.carsharingapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {
    private static final String RENTAL_DATE_FIELD_NAME = "rentalDate";
    private static final String RETURN_DATE_FIELD_NAME = "returnDate";

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }
        try {
            LocalDate rentalDate = (LocalDate) getProperty(dto, RENTAL_DATE_FIELD_NAME);
            LocalDate returnDate = (LocalDate) getProperty(dto, RETURN_DATE_FIELD_NAME);
            return rentalDate.isBefore(returnDate);
        } catch (Exception e) {
            throw new RuntimeException("Exception during data validation.", e);
        }
    }

    private Object getProperty(Object object, String propertyName) throws NoSuchFieldException,
            IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field field = clazz.getDeclaredField(propertyName);
        field.setAccessible(true);
        return field.get(object);
    }
}
