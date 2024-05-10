package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.AddEmployeeToCompanyCmd;
import com.example.domain.entity.Company;
import com.example.domain.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddEmployeeToCompanyHandlerImpl implements AddEmployeeToCompanyHandler {

  private final CompanyService repository;

  @Override
  public Company addEmployeeToCompany(AddEmployeeToCompanyCmd addEmployeeToCompanyCmd) {
    return repository.addEmployeeToCompany(addEmployeeToCompanyCmd.getNif(), addEmployeeToCompanyCmd.getCif());
  }
}
