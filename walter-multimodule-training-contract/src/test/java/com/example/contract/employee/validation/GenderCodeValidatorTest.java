package com.example.contract.employee.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GenderCodeValidatorTest {

  private final GenderCodeValidator genderCodeValidator = new GenderCodeValidator();

  @Test
  @DisplayName("Verify valid gender codes")
  void validGenderCodesTest() {
    Assertions.assertTrue(genderCodeValidator.isValid(1, null));
    Assertions.assertTrue(genderCodeValidator.isValid(2, null));
  }

  @Test
  @DisplayName("Reject invalid gender codes")
  void invalidGenderCodesTest() {
    Assertions.assertFalse(genderCodeValidator.isValid(0, null));
    Assertions.assertFalse(genderCodeValidator.isValid(4, null));
    Assertions.assertFalse(genderCodeValidator.isValid(-1, null));
    Assertions.assertTrue(genderCodeValidator.isValid(null, null));
  }
}