package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.cmd.EmployeeCreateCmd;
import com.example.domain.entity.Employee;

public interface EmployeeCreateHandler {

  Employee addEmployee(EmployeeCreateCmd employeeCreateCmd);
}
