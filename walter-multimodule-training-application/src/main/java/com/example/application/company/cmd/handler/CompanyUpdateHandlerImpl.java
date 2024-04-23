package com.example.application.company.cmd.handler;

import com.example.application.company.cmd.dto.CompanyUpdateCmd;
import com.example.domain.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyUpdateHandlerImpl implements CompanyUpdateHandler {

  @Override
  public Company updateCompany(CompanyUpdateCmd companyUpdateCmd) {
    return null;
  }
}
