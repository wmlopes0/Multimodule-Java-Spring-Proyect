package com.example.application.company.cmd.handler;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.application.company.mapper.CompanyApplicationMapper;
import com.example.domain.entity.Company;
import com.example.domain.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyCreateHandlerImpl implements CompanyCreateHandler {

  private final CompanyService service;

  private final CompanyApplicationMapper mapper;

  @Override
  public Company addCompany(CompanyCreateCmd companyCreateCmd) {
    return service.createCompany(
        mapper.mapToCompanyCreateVO(companyCreateCmd));
  }
}
