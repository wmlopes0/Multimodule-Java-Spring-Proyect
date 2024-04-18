package com.example.contract.employee.controller;

import com.example.domain.exception.EmployeeNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @PostMapping("/test/validation")
  public void testValidation(@Valid @RequestBody TestRequest request) {

  }

  @GetMapping("/test/runtimeException")
  public void testRuntimeException() {
    throw new RuntimeException("Runtime exception occurred");
  }

  @GetMapping("/test/employeeNotFound")
  public void testEmployeeNotFound() {
    throw new EmployeeNotFoundException("Employee not found");
  }

  @Getter
  @Setter
  static class TestRequest {

    @NotNull(message = "must not be empty")
    private String name;

  }
}