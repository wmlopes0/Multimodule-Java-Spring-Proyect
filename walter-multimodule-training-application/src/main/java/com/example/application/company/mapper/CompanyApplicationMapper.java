package com.example.application.company.mapper;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.application.company.cmd.dto.CompanyUpdateCmd;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyApplicationMapper {

  CompanyCreateVO mapToCompanyCreateVO(CompanyCreateCmd companyCreateCmd);

  CompanyUpdateVO mapToCompanyUpdateVO(CompanyUpdateCmd companyUpdateCmd);
}
