package com.example.infrastructure.service;

import java.util.List;

import com.example.domain.entity.Company;
import com.example.domain.service.CompanyService;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyRepositoryServiceImpl implements CompanyService {

  @Override
  public Company createCompany(CompanyCreateVO companyCreateVO) {
    return null;
  }

  @Override
  public Company updateCompany(CompanyUpdateVO companyUpdateVO) {
    return null;
  }

  @Override
  public Company deleteCompany(String cif) {
    return null;
  }

  @Override
  public Company getCompany(String cif) {
    return null;
  }

  @Override
  public List<Company> getCompanies() {
    return List.of();
  }

  @Override
  public Company addEmployeeToCompany(String nif, String cif) {
    return null;
  }

  @Override
  public Company removeEmployeeFromCompany(String nif, String cif) {
    return null;
  }
}
