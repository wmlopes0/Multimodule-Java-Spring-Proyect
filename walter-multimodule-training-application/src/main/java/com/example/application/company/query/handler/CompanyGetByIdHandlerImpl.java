package com.example.application.company.query.handler;

import com.example.application.company.query.dto.CompanyByIdQuery;
import com.example.domain.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyGetByIdHandlerImpl implements CompanyGetByIdHandler {

  @Override
  public Company getCompanyById(CompanyByIdQuery companyByIdQuery) {
    return null;
  }
}
