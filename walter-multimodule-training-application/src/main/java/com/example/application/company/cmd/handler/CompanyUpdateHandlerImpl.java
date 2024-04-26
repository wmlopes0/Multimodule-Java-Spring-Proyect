package com.example.application.company.cmd.handler;

import com.example.application.company.cmd.dto.CompanyUpdateCmd;
import com.example.application.company.mapper.CompanyApplicationMapper;
import com.example.domain.entity.Company;
import com.example.domain.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyUpdateHandlerImpl implements CompanyUpdateHandler {

  private final CompanyService service;

  private final CompanyApplicationMapper mapper;

  @Override
  public Company updateCompany(CompanyUpdateCmd companyUpdateCmd) {
    return service.updateCompany(
        mapper.mapToCompanyUpdateVO(companyUpdateCmd));
  }
}
