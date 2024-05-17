package com.example.contract.employee.validation;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class YearOfBirthValidatorTest {

  private final YearOfBirthValidator yearOfBirthValidator = new YearOfBirthValidator();

  @Test
  @DisplayName("Validate correct years of birth")
  void validYearsOfBirthTest() {
    int currentYear = LocalDate.now().getYear();
    Assertions.assertTrue(yearOfBirthValidator.isValid(1900, null));
    Assertions.assertTrue(yearOfBirthValidator.isValid(currentYear, null));
    Assertions.assertTrue(yearOfBirthValidator.isValid(2000, null));
    Assertions.assertTrue(yearOfBirthValidator.isValid(null, null));
  }

  @Test
  @DisplayName("Reject incorrect years of birth")
  void invalidYearsOfBirthTest() {
    int currentYear = LocalDate.now().getYear();
    Assertions.assertFalse(yearOfBirthValidator.isValid(1899, null));
    Assertions.assertFalse(yearOfBirthValidator.isValid(currentYear + 1, null));
  }
}