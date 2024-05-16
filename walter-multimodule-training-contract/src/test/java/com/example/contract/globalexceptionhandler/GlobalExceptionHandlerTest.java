package com.example.contract.globalexceptionhandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.example.contract.employee.controller.GlobalExceptionHandler;
import com.example.domain.exception.EmployeeNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.MethodValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @ParameterizedTest
  @CsvSource(value = {
      "45134320W,nif,Invalid NIF",
      "null,nif,NIF cannot be null",
      "'',name,Name cannot be empty",
      "null,name,Name cannot be null",
      "wmlopes0gmail.com,email,Invalid email format",
      "invalidGender,gender,Gender must be either 'MALE' or 'FEMALE'",
      "null,gender,Gender cannot be null",
      "null,personalPhone,Personal phone cannot be null",
      "458485485485,personalPhone,Invalid phone format",
      "45485,companyPhone,Invalid phone format",
      "45485,default,Invalid NIF"
  }, nullValues = {"null"})
  @DisplayName("Handle MethodArgumentNotValidException")
  void handleValidationExceptions(String value, String field, String expectedMessage) {
    FieldError fieldError = new FieldError("objectName", field, value, false, null, null, "Invalid NIF");
    BindingResult bindingResult = mock(BindingResult.class);
    when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

    MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
    ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().containsKey(field));
    assertEquals(expectedMessage, response.getBody().get(field));
  }

  @Test
  @DisplayName("Handle MethodArgumentTypeMismatchException")
  void handleTypeMismatchExceptions() {
    MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
    when(ex.getName()).thenReturn("param");
    when(ex.getValue()).thenReturn("invalidValue");

    ResponseEntity<Map<String, String>> response = handler.handleTypeMismatchExceptions(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().containsKey("param"));
    assertEquals("Invalid parameter: invalidValue", response.getBody().get("param"));
  }

  @Test
  @DisplayName("Handle ConstraintViolationException")
  void handleConstraintViolationExceptions() {
    ConstraintViolation<?> violation = mock(ConstraintViolation.class);

    Path propertyPath = mock(Path.class);
    when(propertyPath.toString()).thenReturn("nif");
    when(violation.getPropertyPath()).thenReturn(propertyPath);
    when(violation.getMessage()).thenReturn("must not be null");

    Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
    ConstraintViolationException ex = new ConstraintViolationException(violations);

    ResponseEntity<Map<String, String>> response = handler.handleConstraintViolationExceptions(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().containsKey("nif"));
    assertEquals("must not be null", response.getBody().get("nif"));
  }

  @Test
  @DisplayName("Handle RuntimeException")
  void handleRuntimeException() {
    RuntimeException ex = new RuntimeException("An error occurred");

    ResponseEntity<Map<String, String>> response = handler.handleRuntimeException(ex);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertTrue(response.getBody().containsKey("error"));
    assertEquals("Internal Server Error", response.getBody().get("error"));
    assertEquals("An error occurred", response.getBody().get("message"));
  }

  @Test
  @DisplayName("Handle EmployeeNotFoundException")
  void handleEmployeeNotFoundException() {
    EmployeeNotFoundException ex = new EmployeeNotFoundException("Employee not found");

    ResponseEntity<Map<String, String>> response = handler.handleEmployeeNotFoundException(ex);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  @DisplayName("Handle HandlerMethodValidationException")
  void handleHandlerMethodValidationException() {
    // Crear un mock para ConstraintViolation
    ConstraintViolation<?> violation = mock(ConstraintViolation.class);

    // Crear un mock para Path
    Path propertyPath = mock(Path.class);
    when(propertyPath.toString()).thenReturn("field");

    // Configurar el mock de ConstraintViolation para devolver el mock de Path
    when(violation.getPropertyPath()).thenReturn(propertyPath);
    when(violation.getMessage()).thenReturn("must not be null");

    // Crear un mock para MethodValidationResult
    MethodValidationResult validationResult = mock(MethodValidationResult.class);
    when(validationResult.isForReturnValue()).thenReturn(false);

    // Crear el HandlerMethodValidationException con el mock de MethodValidationResult
    HandlerMethodValidationException ex = new HandlerMethodValidationException(validationResult);

    ResponseEntity<Map<String, String>> response = handler.handleHandlerMethodValidationException(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertFalse(response.getBody().containsKey("field"));
    assertEquals("Invalid NIF", response.getBody().get("nif"));
  }
}
