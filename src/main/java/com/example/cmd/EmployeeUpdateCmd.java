package com.example.cmd;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeUpdateCmd {

  private Long id;

  private String name;
}
