package com.example.application.company.cmd.handler;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.domain.entity.Company;

public interface CompanyCreateHandler {

  Company addCompany(CompanyCreateCmd companyCreateCmd);
}
