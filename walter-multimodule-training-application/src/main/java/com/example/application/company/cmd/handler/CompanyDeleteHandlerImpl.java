package com.example.application.company.cmd.handler;

import com.example.application.company.cmd.dto.CompanyDeleteCmd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyDeleteHandlerImpl implements CompanyDeleteHandler {

  @Override
  public boolean deleteCompany(CompanyDeleteCmd companyDeleteCmd) {
    return false;
  }
}
