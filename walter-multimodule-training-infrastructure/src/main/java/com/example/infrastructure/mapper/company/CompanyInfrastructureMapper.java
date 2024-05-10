package com.example.infrastructure.mapper.company;

import com.example.domain.entity.Company;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import com.example.infrastructure.entity.CompanyEntity;

public interface CompanyInfrastructureMapper {

  Company mapToDomain(CompanyEntity companyEntity);

  CompanyEntity mapToEntity(CompanyCreateVO companyCreateVO);

  CompanyEntity mapToEntity(CompanyUpdateVO companyUpdateVO);
}
