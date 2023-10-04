package com.jvavateam.carsharingapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch,
        Object> {
    private String firstFieldName;
    private String secondFieldName;

    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object firstElement = new BeanWrapperImpl(value)
                .getPropertyValue(firstFieldName);
        Object secondElement = new BeanWrapperImpl(value)
                .getPropertyValue(secondFieldName);
        return Objects.equals(firstElement, secondElement);
    }
}
