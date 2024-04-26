package com.example.application.company.cmd.handler;

import com.example.application.company.cmd.dto.CompanyDeleteCmd;
import com.example.domain.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyDeleteHandlerImpl implements CompanyDeleteHandler {

  private final CompanyService service;

  @Override
  public boolean deleteCompany(CompanyDeleteCmd companyDeleteCmd) {
    return service.deleteCompany(companyDeleteCmd.getCif());
  }
}
