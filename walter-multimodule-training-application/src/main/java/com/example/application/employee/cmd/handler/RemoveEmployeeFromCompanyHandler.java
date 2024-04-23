package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.RemoveEmployeeFromCompanyCmd;

public interface RemoveEmployeeFromCompanyHandler {

  boolean removeEmployeeFromCompany(RemoveEmployeeFromCompanyCmd removeEmployeeFromCompanyCmd);

}
