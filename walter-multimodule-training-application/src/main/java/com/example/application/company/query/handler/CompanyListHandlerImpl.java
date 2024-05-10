package com.example.application.company.query.handler;

import java.util.List;

import com.example.domain.entity.Company;
import com.example.domain.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyListHandlerImpl implements CompanyListHandler {

  private final CompanyService service;

  @Override
  public List<Company> listCompanies() {
    return service.getCompanies();
  }
}
