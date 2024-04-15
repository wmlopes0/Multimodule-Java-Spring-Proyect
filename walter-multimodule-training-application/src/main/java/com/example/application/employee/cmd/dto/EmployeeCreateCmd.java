package com.example.application.employee.cmd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeCreateCmd {

  private String nif;

  private String name;

  private String surname;

  private int birthYear;

  private String gender;

  private String personalPhone;

  private String companyPhone;

  private String email;
}
