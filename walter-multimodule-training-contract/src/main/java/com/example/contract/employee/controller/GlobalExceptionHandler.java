package com.example.contract.employee.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.domain.exception.EmployeeNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      String errorMessage = switch (error.getField()) {
        case "nif" -> error.getRejectedValue() == null ? "NIF cannot be null" : "Invalid NIF";
        case "name" -> error.getRejectedValue() == null ? "Name cannot be null" : "Name cannot be empty";
        case "email" -> "Invalid email format";
        case "gender" -> error.getRejectedValue() == null ? "Gender cannot be null" : "Gender must be either 'MALE' or 'FEMALE'";
        case "personalPhone" -> error.getRejectedValue() == null ? "Personal phone cannot be null" : "Invalid phone format";
        case "companyPhone" -> "Invalid phone format";
        default -> error.getDefaultMessage();
      };
      errors.put(error.getField(), errorMessage);
    }
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, String>> handleTypeMismatchExceptions(MethodArgumentTypeMismatchException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put(ex.getName(), "Invalid parameter: " + ex.getValue());
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationExceptions(ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations().forEach(violation -> {
      String fieldName = violation.getPropertyPath().toString();
      errors.put(fieldName, violation.getMessage());
    });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
    Map<String, String> errorDetails = new HashMap<>();
    errorDetails.put("error", "Internal Server Error");
    errorDetails.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
  }

  @ExceptionHandler(EmployeeNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Map<String, String>> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  }

  @ExceptionHandler(HandlerMethodValidationException.class)
  public ResponseEntity<Map<String, String>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("nif", "Invalid NIF");
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

}