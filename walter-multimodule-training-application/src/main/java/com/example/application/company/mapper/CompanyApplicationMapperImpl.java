package com.example.application.company.mapper;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.application.company.cmd.dto.CompanyUpdateCmd;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import org.springframework.stereotype.Component;

@Component
public class CompanyApplicationMapperImpl implements CompanyApplicationMapper {

  @Override
  public CompanyCreateVO mapToCompanyCreateVO(CompanyCreateCmd companyCreateCmd) {
    return CompanyCreateVO.builder()
        .cif(companyCreateCmd.getCif())
        .name(companyCreateCmd.getName())
        .build();
  }

  @Override
  public CompanyUpdateVO mapToCompanyUpdateVO(CompanyUpdateCmd companyUpdateCmd) {
    return CompanyUpdateVO.builder()
        .cif(companyUpdateCmd.getCif())
        .name(companyUpdateCmd.getName())
        .build();
  }
}
