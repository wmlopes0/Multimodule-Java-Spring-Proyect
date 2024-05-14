package com.example.contract.employee.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.domain.exception.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    String msg;
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      switch (error.getField()) {
        case "nif":
          msg = error.getRejectedValue() == null ? "NIF cannot be null" : "Invalid NIF";
          errors.put(error.getField(), msg);
          break;
        case "name":
          msg = error.getRejectedValue() == null ? "Name cannot be null" : "Name cannot be empty";
          errors.put(error.getField(), msg);
          break;
        case "email":
          errors.put(error.getField(), "Invalid email format");
          break;
        case "gender":
          msg = error.getRejectedValue() == null ? "Gender cannot be null" : "Gender must be either 'MALE' or 'FEMALE'";
          errors.put(error.getField(), msg);
          break;
        case "personalPhone":
          msg = error.getRejectedValue() == null ? "Personal phone cannot be null" : "Invalid phone format";
          errors.put(error.getField(), msg);
          break;
        case "companyPhone":
          errors.put(error.getField(), "Invalid phone format");
          break;
        default:
          errors.put(error.getField(), error.getDefaultMessage());
      }
    }
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

}