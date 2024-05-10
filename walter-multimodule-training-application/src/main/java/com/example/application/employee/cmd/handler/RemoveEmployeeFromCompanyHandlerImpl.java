package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.RemoveEmployeeFromCompanyCmd;
import com.example.domain.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveEmployeeFromCompanyHandlerImpl implements RemoveEmployeeFromCompanyHandler {

  private final CompanyService repository;

  @Override
  public boolean removeEmployeeFromCompany(RemoveEmployeeFromCompanyCmd removeEmployeeFromCompanyCmd) {
    return repository.removeEmployeeFromCompany(removeEmployeeFromCompanyCmd.getNif(), removeEmployeeFromCompanyCmd.getCif());
  }
}
