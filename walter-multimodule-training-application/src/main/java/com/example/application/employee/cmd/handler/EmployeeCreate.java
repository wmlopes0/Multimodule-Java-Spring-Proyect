package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.EmployeeCreateCmd;
import com.example.domain.entity.Employee;

public interface EmployeeCreate {
  Employee addEmployee(EmployeeCreateCmd employeeCreateCmd);
}
