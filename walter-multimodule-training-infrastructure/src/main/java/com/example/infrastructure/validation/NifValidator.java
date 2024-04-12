package com.example.infrastructure.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NifValidator implements ConstraintValidator<ValidNIF, String> {

  private static final String DNI_PATTERN = "\\d{8}[A-Z]";

  private static final String NIE_PATTERN = "[XYZ]\\d{7}[A-Z]";

  private static final char[] LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE".toCharArray();

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }

    return (value.matches(DNI_PATTERN) && isValidDni(value)) || (value.matches(NIE_PATTERN) && isValidNie(value));
  }

  private boolean isValidDni(String dni) {
    int number = Integer.parseInt(dni.substring(0, 8));
    char letter = dni.charAt(8);
    int index = number % 23;
    return LETTERS[index] == letter;
  }

  private boolean isValidNie(String nie) {
    int number = Integer.parseInt(nie.substring(1, 8));
    char prefix = nie.charAt(0);
    char letter = nie.charAt(8);

    switch (prefix) {
      case 'X':
        number += 10000000;
        break;
      case 'Y':
        number += 11000000;
        break;
      case 'Z':
        number += 12000000;
        break;
      default:
        break;
    }

    int index = number % 23;
    return LETTERS[index] == letter;
  }
}