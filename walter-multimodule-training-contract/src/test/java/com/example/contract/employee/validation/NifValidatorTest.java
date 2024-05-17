package com.example.contract.employee.validation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NifValidatorTest {

  private final NifValidator nifValidator = new NifValidator();

  @Test
  @DisplayName("Validate correct DNI format")
  void validDniTest() {
    Assertions.assertTrue(nifValidator.isValid("45134320V", null));
  }

  @Test
  @DisplayName("Reject incorrect DNI formats")
  void invalidDniTest() {
    Assertions.assertFalse(nifValidator.isValid("45134320Y", null));
    Assertions.assertFalse(nifValidator.isValid("1234567Z", null));
    Assertions.assertFalse(nifValidator.isValid("123456789Z", null));
    Assertions.assertFalse(nifValidator.isValid("12345678", null));
    Assertions.assertFalse(nifValidator.isValid("ABCDEFGHJ", null));
  }

  @Test
  @DisplayName("Validate correct NIE format")
  void validNieTest() {
    Assertions.assertTrue(nifValidator.isValid("Z9747924T", null));
    Assertions.assertTrue(nifValidator.isValid("Z9813063A", null));
    Assertions.assertTrue(nifValidator.isValid("Z0892136V", null));
    Assertions.assertTrue(nifValidator.isValid("Y8683832N", null));
    Assertions.assertTrue(nifValidator.isValid("X6001944W", null));
  }

  @Test
  @DisplayName("Reject incorrect NIE formats")
  void invalidNieTest() {
    Assertions.assertFalse(nifValidator.isValid("X0582338W", null));
    Assertions.assertFalse(nifValidator.isValid("1234567X", null));
    Assertions.assertFalse(nifValidator.isValid("Y12345678L", null));
    Assertions.assertFalse(nifValidator.isValid("Z123456L", null));
    Assertions.assertFalse(nifValidator.isValid("Z9813063W", null));
  }

  @Test
  @DisplayName("Handle null and empty NIF values")
  void nullAndEmptyValuesTest() {
    Assertions.assertTrue(nifValidator.isValid(null, null));
    Assertions.assertFalse(nifValidator.isValid("", null));
  }

}