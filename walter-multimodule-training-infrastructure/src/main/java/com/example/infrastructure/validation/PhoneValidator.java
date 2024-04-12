package com.example.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

  private static final String PHONE_PATTERN = "^\\+\\d{2}\\d{9}$";

  @Override
  public boolean isValid(String phoneField, ConstraintValidatorContext context) {
    if (phoneField == null) {
      return false; // or true based on your requirement if null is acceptable
    }
    return phoneField.matches(PHONE_PATTERN);
  }
}
