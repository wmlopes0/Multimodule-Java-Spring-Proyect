package com.example.domain.exception;

public class EmployeeNotFoundException extends RuntimeException {

  public EmployeeNotFoundException(String message) {
    super(message);
  }
}