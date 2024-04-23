package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.AddEmployeeToCompanyCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddEmployeeToCompanyHandlerImpl implements AddEmployeeToCompanyHandler {

  @Override
  public boolean addEmployeeToCompany(AddEmployeeToCompanyCmd addEmployeeToCompanyCmd) {
    return false;
  }
}
