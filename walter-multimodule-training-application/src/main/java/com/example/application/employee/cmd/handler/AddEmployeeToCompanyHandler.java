package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.AddEmployeeToCompanyCmd;
import com.example.domain.entity.Company;

public interface AddEmployeeToCompanyHandler {

  Company addEmployeeToCompany(AddEmployeeToCompanyCmd addEmployeeToCompanyCmd);

}
