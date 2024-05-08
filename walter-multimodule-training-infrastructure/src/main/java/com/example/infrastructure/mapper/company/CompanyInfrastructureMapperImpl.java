package com.example.infrastructure.mapper.company;

import com.example.domain.entity.Company;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import com.example.infrastructure.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyInfrastructureMapperImpl implements CompanyInfrastructureMapper {

  @Override
  public Company mapToDomain(CompanyEntity companyEntity) {
    return new Company()
        .setCif(companyEntity.getCif())
        .setName(companyEntity.getName());
  }

  @Override
  public CompanyEntity mapToEntity(CompanyCreateVO companyCreateVO) {
    return new CompanyEntity()
        .setCif(companyCreateVO.getCif())
        .setName(companyCreateVO.getName());
  }

  @Override
  public CompanyEntity mapToEntity(CompanyUpdateVO companyUpdateVO) {
    return new CompanyEntity()
        .setCif(companyUpdateVO.getCif())
        .setName(companyUpdateVO.getName());
  }
}
