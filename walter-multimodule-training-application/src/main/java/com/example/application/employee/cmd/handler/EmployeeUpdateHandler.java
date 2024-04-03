package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.domain.entity.Employee;

public interface EmployeeUpdateHandler {

  Employee updateEmployee(EmployeeUpdateCmd employeeUpdateCmd);
}
