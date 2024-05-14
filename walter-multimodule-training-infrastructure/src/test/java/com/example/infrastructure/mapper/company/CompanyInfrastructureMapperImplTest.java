package com.example.infrastructure.mapper.company;

import com.example.domain.entity.Company;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import com.example.infrastructure.entity.CompanyEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CompanyInfrastructureMapperImplTest {

  private final CompanyInfrastructureMapper companyInfrastructureMapper = Mappers.getMapper(CompanyInfrastructureMapper.class);

  @Test
  @DisplayName("Mapping CompanyEntity to Company correctly")
  void mapToDomainTest() {
    CompanyEntity companyEntity = new CompanyEntity()
        .setCif("V33778580")
        .setName("Company S.L");
    Company expected = new Company()
        .setCif("V33778580")
        .setName("Company S.L");

    Company result = companyInfrastructureMapper.mapToDomain(companyEntity);
    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping CompanyCreateVO to CompanyEntity correctly")
  void mapComanyCreateVOToEntityTest() {
    CompanyCreateVO companyCreateVO = CompanyCreateVO.builder()
        .cif("V33778580")
        .name("Company S.L")
        .build();
    CompanyEntity expected = new CompanyEntity()
        .setCif("V33778580")
        .setName("Company S.L");

    CompanyEntity result = companyInfrastructureMapper.mapToEntity(companyCreateVO);
    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping CompanyUpdateVO to CompanyEntity correctly")
  void mapComanyUpdateVOToEntityTest() {
    CompanyUpdateVO companyUpdateVO = CompanyUpdateVO.builder()
        .cif("V33778580")
        .name("Company S.L")
        .build();
    CompanyEntity expected = new CompanyEntity()
        .setCif("V33778580")
        .setName("Company S.L");

    CompanyEntity result = companyInfrastructureMapper.mapToEntity(companyUpdateVO);
    Assertions.assertEquals(expected, result);
  }
}