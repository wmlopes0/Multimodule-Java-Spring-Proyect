package com.example.application.company.cmd.handler;

import com.example.application.company.cmd.dto.CompanyDeleteCmd;

public interface CompanyDeleteHandler {

  boolean deleteCompany(CompanyDeleteCmd companyDeleteCmd);
}
