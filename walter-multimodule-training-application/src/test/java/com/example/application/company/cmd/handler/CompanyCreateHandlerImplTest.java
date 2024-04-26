package com.example.application.company.cmd.handler;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.application.company.mapper.CompanyApplicationMapper;
import com.example.domain.entity.Company;
import com.example.domain.service.CompanyService;
import com.example.domain.vo.company.CompanyCreateVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyCreateHandlerImplTest {

  @Mock
  private CompanyService service;

  @Mock
  private CompanyApplicationMapper mapper;

  @InjectMocks
  private CompanyCreateHandlerImpl handler;

  @ParameterizedTest
  @MethodSource("addCompanyParameters")
  @DisplayName("Add new Company successfully")
  void addCompanyTest(CompanyCreateCmd companyCreateCmd, CompanyCreateVO companyCreateVO, Company company) {
    Mockito.when(mapper.mapToCompanyCreateVO(companyCreateCmd)).thenReturn(companyCreateVO);
    Mockito.when(service.createCompany(companyCreateVO)).thenReturn(company);

    Company result = handler.addCompany(companyCreateCmd);
    Assertions.assertEquals(company, result);

    Mockito.verify(mapper, Mockito.times(1)).mapToCompanyCreateVO(companyCreateCmd);
    Mockito.verify(service, Mockito.times(1)).createCompany(companyCreateVO);
  }

  private static Stream<Arguments> addCompanyParameters() {
    return Stream.of(
        Arguments.of(
            new CompanyCreateCmd("Q4947066I", "Company1 S.L"),
            CompanyCreateVO.builder()
                .cif("Q4947066I")
                .name("Company1 S.L")
                .employees(new ArrayList<>())
                .build(),
            new Company()
                .setCif("Q4947066I")
                .setName("Company1 S.L")
                .setEmployees(new ArrayList<>()))
    );
  }

}