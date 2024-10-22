package com.example.contract.employee.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NifValidator implements ConstraintValidator<ValidNIF, String> {

  private static final String DNI_PATTERN = "\\d{8}[A-Z]";

  private static final String NIE_PATTERN = "[XYZ]\\d{7}[A-Z]";

  private static final char[] LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE".toCharArray();

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
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
    int number;
    char prefix = nie.charAt(0);
    char letter = nie.charAt(8);

    if (prefix == 'X') {
      number = Integer.parseInt("0" + nie.substring(1, 8));
    } else if (prefix == 'Y') {
      number = Integer.parseInt("1" + nie.substring(1, 8));
    } else {
      number = Integer.parseInt("2" + nie.substring(1, 8));
    }

    int index = number % 23;
    return LETTERS[index] == letter;
  }
}