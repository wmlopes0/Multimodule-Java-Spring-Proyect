package com.example.application.company.mapper;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.application.company.cmd.dto.CompanyUpdateCmd;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CompanyApplicationMapperImplTest {

  private final CompanyApplicationMapper mapper = Mappers.getMapper(CompanyApplicationMapper.class);

  @Test
  @DisplayName("Mapping CompanyCreateCmd to CompanyCreateVO correctly")
  void mapToCompanyCreateVO() {
    CompanyCreateCmd companyCreateCmd = new CompanyCreateCmd()
        .setCif("V33778580")
        .setName("Company S.L");
    CompanyCreateVO expected = CompanyCreateVO.builder()
        .cif("V33778580")
        .name("Company S.L")
        .build();
    CompanyCreateVO result = mapper.mapToCompanyCreateVO(companyCreateCmd);
    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping CompanyUpdateCmd to CompanyUpdateVO correctly")
  void mapToCompanyUpdateVO() {
    CompanyUpdateCmd companyUpdateCmd = new CompanyUpdateCmd()
        .setCif("V33778580")
        .setName("Company S.L");
    CompanyUpdateVO expected = CompanyUpdateVO.builder()
        .cif("V33778580")
        .name("Company S.L")
        .build();
    CompanyUpdateVO result = mapper.mapToCompanyUpdateVO(companyUpdateCmd);
    Assertions.assertEquals(expected, result);
  }
}

