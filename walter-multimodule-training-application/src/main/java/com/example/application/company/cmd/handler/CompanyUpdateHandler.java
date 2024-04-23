package com.example.application.company.cmd.handler;

import com.example.application.company.cmd.dto.CompanyUpdateCmd;
import com.example.domain.entity.Company;

public interface CompanyUpdateHandler {

  Company updateCompany(CompanyUpdateCmd companyUpdateCmd);
}
