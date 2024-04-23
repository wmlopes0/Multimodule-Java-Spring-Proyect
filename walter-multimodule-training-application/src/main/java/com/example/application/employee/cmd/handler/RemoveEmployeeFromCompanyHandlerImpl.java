package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.RemoveEmployeeFromCompanyCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveEmployeeFromCompanyHandlerImpl implements RemoveEmployeeFromCompanyHandler {

  @Override
  public boolean removeEmployeeFromCompany(RemoveEmployeeFromCompanyCmd removeEmployeeFromCompanyCmd) {
    return false;
  }
}
