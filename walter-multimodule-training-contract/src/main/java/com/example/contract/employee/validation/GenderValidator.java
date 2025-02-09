package com.example.contract.employee.validation;

import com.example.domain.entity.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<ValidGender, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    try {
      Gender.valueOf(value.toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}