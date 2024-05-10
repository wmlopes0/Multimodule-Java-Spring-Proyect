package com.example.domain.service;

import java.util.List;

import com.example.domain.entity.Company;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;

public interface CompanyService {

  Company createCompany(CompanyCreateVO companyCreateVO);

  Company updateCompany(CompanyUpdateVO companyUpdateVO);

  boolean deleteCompany(String cif);

  Company getCompany(String cif);

  List<Company> getCompanies();

  Company addEmployeeToCompany(String nif, String cif);

  boolean removeEmployeeFromCompany(String nif, String cif);
}
