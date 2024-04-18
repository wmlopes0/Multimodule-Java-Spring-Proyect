package com.example.contract.employee.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GenderValidatorTest {

  private final GenderValidator genderValidator = new GenderValidator();

  @Test
  @DisplayName("Validate correct gender values")
  void validGenderTest() {
    Assertions.assertTrue(genderValidator.isValid("MALE", null));
    Assertions.assertTrue(genderValidator.isValid("FEMALE", null));
    Assertions.assertTrue(genderValidator.isValid("male", null));
    Assertions.assertTrue(genderValidator.isValid("female", null));
    Assertions.assertTrue(genderValidator.isValid("MaLe", null));
    Assertions.assertTrue(genderValidator.isValid("FeMaLe", null));
  }

  @Test
  @DisplayName("Reject incorrect gender values")
  void invalidGenderTest() {
    Assertions.assertFalse(genderValidator.isValid("man", null));
    Assertions.assertFalse(genderValidator.isValid("woman", null));
    Assertions.assertFalse(genderValidator.isValid("MALES", null));
    Assertions.assertFalse(genderValidator.isValid("123", null));
    Assertions.assertFalse(genderValidator.isValid("", null));
    Assertions.assertFalse(genderValidator.isValid("  ", null));
    Assertions.assertTrue(genderValidator.isValid(null, null));
  }
}