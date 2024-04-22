package com.example.application.employee.cmd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
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
