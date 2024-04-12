package com.example.infrastructure.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PhoneValidatorTest {

  private final PhoneValidator phoneValidator = new PhoneValidator();

  @Test
  @DisplayName("Validate correct phone numbers")
  void validPhoneNumbersTest() {
    Assertions.assertTrue(phoneValidator.isValid("+34123456789", null));
    Assertions.assertTrue(phoneValidator.isValid("+44098765432", null));
  }

  @Test
  @DisplayName("Reject incorrect phone numbers")
  void invalidPhoneNumbersTest() {
    Assertions.assertFalse(phoneValidator.isValid("1234567890", null));
    Assertions.assertFalse(phoneValidator.isValid("+34 123456789", null));
    Assertions.assertFalse(phoneValidator.isValid("+341234567890", null));
    Assertions.assertFalse(phoneValidator.isValid(null, null));
    Assertions.assertFalse(phoneValidator.isValid("+3412", null));
  }
}