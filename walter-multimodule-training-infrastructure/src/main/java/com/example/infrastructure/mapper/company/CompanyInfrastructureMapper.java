package com.example.infrastructure.mapper.company;

import com.example.domain.entity.Company;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import com.example.infrastructure.entity.CompanyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface CompanyInfrastructureMapper {

  Company mapToDomain(CompanyEntity companyEntity);

  CompanyEntity mapToEntity(CompanyCreateVO companyCreateVO);

  CompanyEntity mapToEntity(CompanyUpdateVO companyUpdateVO);
}
