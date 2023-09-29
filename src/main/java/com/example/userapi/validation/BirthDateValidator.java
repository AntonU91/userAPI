package com.example.userapi.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.beans.factory.annotation.Value;

public class BirthDateValidator implements ConstraintValidator<BirthDate, LocalDate> {
    @Value(value = "${user.minimal.age}")
    private int minimalAge;

    @Override public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        return age.getYears() > minimalAge;
    }
}
