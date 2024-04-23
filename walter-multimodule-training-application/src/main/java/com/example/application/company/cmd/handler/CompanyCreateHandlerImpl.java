package com.example.application.company.cmd.handler;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.domain.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyCreateHandlerImpl implements CompanyCreateHandler {

  @Override
  public Company addCompany(CompanyCreateCmd companyCreateCmd) {
    return null;
  }
}
