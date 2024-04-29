package com.example.contract.employee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.domain.exception.EmployeeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({MethodArgumentNotValidException.class, HandlerMethodValidationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      Exception ex) {
    Map<String, String> errors = new HashMap<>();

    if (ex instanceof MethodArgumentNotValidException methodArgEx) {
      BindingResult bindingResult = methodArgEx.getBindingResult();
      bindingResult.getAllErrors().forEach(error -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      });
    } else if (ex instanceof HandlerMethodValidationException handlerMethodEx) {
      List<ParameterValidationResult> validationResults = handlerMethodEx.getAllValidationResults();
      for (ParameterValidationResult validationResult : validationResults) {
        validationResult.getResolvableErrors().forEach(error -> {
          String fieldName = validationResult.getMethodParameter().getParameterName();
          String errorMessage = error.getDefaultMessage();
          errors.put(fieldName != null ? fieldName : "unknown", errorMessage);
        });
      }
    }

    return ResponseEntity.badRequest().body(errors);
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