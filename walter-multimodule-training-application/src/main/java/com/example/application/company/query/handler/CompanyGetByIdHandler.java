package com.example.application.company.query.handler;

import com.example.application.company.query.dto.CompanyByIdQuery;
import com.example.domain.entity.Company;

public interface CompanyGetByIdHandler {

  Company getCompanyById(CompanyByIdQuery companyByIdQuery);
}
