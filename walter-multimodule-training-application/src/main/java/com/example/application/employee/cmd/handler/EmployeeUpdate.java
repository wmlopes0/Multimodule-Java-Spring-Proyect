package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.EmployeeUpdateCmd;
import com.example.domain.entity.Employee;

public interface EmployeeUpdate {

  Employee updateEmployee(EmployeeUpdateCmd employeeUpdateCmd);
}
