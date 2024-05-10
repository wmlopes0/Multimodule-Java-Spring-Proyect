package com.example.contract.company.mapper;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.application.company.cmd.dto.CompanyDeleteCmd;
import com.example.application.company.cmd.dto.CompanyUpdateCmd;
import com.example.application.company.query.dto.CompanyByIdQuery;
import com.example.contract.company.dto.CompanyRequestDTO;
import com.example.contract.company.dto.CompanyResponseDTO;
import com.example.contract.company.dto.CompanyUpdateDTO;
import com.example.contract.company.dto.EmployeeDTO;
import com.example.domain.entity.Company;
import com.example.domain.entity.Employee;

public interface CompanyContractMapper {

  CompanyCreateCmd mapToCompanyCreateCmd(CompanyRequestDTO companyRequestDTO);

  CompanyDeleteCmd mapToCompanyDeleteCmd(String cif);

  CompanyUpdateCmd mapToCompanyUpdateCmd(String cif, CompanyUpdateDTO companyUpdateDTO);

  CompanyByIdQuery mapToCompanyByIdQuery(String cif);

  CompanyResponseDTO mapToCompanyResponseDTO(Company company);

  EmployeeDTO mapToEmployeeDTO(Employee employee);
}
