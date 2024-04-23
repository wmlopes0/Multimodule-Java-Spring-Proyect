package com.example.application.company.query.handler;

import java.util.List;

import com.example.domain.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyListHandlerImpl implements CompanyListHandler {

  @Override
  public List<Company> listCompanies() {
    return List.of();
  }
}
