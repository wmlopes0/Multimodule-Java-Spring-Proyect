package com.example.application.company.query.handler;

import com.example.application.company.query.dto.CompanyByIdQuery;
import com.example.domain.entity.Company;
import com.example.domain.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyGetByIdHandlerImpl implements CompanyGetByIdHandler {

  private final CompanyService service;

  @Override
  public Company getCompanyById(CompanyByIdQuery companyByIdQuery) {
    return service.getCompany(companyByIdQuery.getCif());
  }
}
