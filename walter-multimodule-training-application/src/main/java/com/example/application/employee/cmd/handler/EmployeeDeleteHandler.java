package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.cmd.EmployeeDeleteCmd;

public interface EmployeeDeleteHandler {

  boolean deleteEmployee(EmployeeDeleteCmd employeeDeleteCmd);
}
