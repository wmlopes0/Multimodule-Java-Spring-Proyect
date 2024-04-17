package com.example.domain.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class YearOfBirthValidator implements ConstraintValidator<ValidYearOfBirth, Integer> {

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    int currentYear = LocalDate.now().getYear();
    return value >= 1900 && value <= currentYear;
  }
}

