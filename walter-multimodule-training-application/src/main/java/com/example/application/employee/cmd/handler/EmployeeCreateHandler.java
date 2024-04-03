package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.domain.entity.Employee;

public interface EmployeeCreateHandler {

  Employee addEmployee(EmployeeCreateCmd employeeCreateCmd);
}
