package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.AddEmployeeToCompanyCmd;

public interface AddEmployeeToCompanyHandler {

  boolean addEmployeeToCompany(AddEmployeeToCompanyCmd addEmployeeToCompanyCmd);

}
