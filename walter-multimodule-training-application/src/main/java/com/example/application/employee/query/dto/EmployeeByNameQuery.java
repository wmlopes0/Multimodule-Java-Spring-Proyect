package com.example.application.employee.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeByNameQuery {

  private String name;
}