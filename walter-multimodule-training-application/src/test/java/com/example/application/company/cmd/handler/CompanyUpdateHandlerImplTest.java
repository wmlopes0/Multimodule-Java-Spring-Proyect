package com.example.application.company.cmd.handler;

import java.util.ArrayList;
import java.util.stream.Stream;

import com.example.application.company.cmd.dto.CompanyUpdateCmd;
import com.example.application.company.mapper.CompanyApplicationMapper;
import com.example.domain.entity.Company;
import com.example.domain.exception.CompanyNotFoundException;
import com.example.domain.service.CompanyService;
import com.example.domain.vo.company.CompanyUpdateVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyUpdateHandlerImplTest {

  @Mock
  private CompanyService service;

  @Mock
  private CompanyApplicationMapper mapper;

  @InjectMocks
  private CompanyUpdateHandlerImpl handler;

  @ParameterizedTest
  @MethodSource("updateCompanyParameters")
  @DisplayName("Update Company information successfully")
  void updateCompanyTest(CompanyUpdateCmd companyUpdateCmd, CompanyUpdateVO companyUpdateVO, Company company) {
    Mockito.when(mapper.mapToCompanyUpdateVO(companyUpdateCmd)).thenReturn(companyUpdateVO);
    Mockito.when(service.updateCompany(companyUpdateVO)).thenReturn(company);

    Company result = handler.updateCompany(companyUpdateCmd);
    Assertions.assertEquals(company, result);

    Mockito.verify(mapper).mapToCompanyUpdateVO(companyUpdateCmd);
    Mockito.verify(service).updateCompany(companyUpdateVO);
  }

  private static Stream<Arguments> updateCompanyParameters() {
    return Stream.of(
        Arguments.of(
            new CompanyUpdateCmd()
                .setCif("Q4947066I")
                .setName("Company1 S.L"),
            CompanyUpdateVO.builder()
                .cif("Q4947066I")
                .name("Company1 S.L")
                .build(),
            new Company()
                .setCif("Q4947066I")
                .setName("Company1 S.L")
                .setEmployees(new ArrayList<>())
        )
    );
  }

  @Test
  @DisplayName("UpdateEmployee Throws CompanyNotFoundException on Error")
  void updateCompanyErrorNotFoundTest() {
    CompanyUpdateCmd companyUpdateCmd = new CompanyUpdateCmd()
        .setCif("Q4947066I")
        .setName("Company1 S.L");
    CompanyUpdateVO companyUpdateVO = CompanyUpdateVO.builder()
        .cif("Q4947066I")
        .name("Company1 S.L")
        .build();

    Mockito.when(mapper.mapToCompanyUpdateVO(companyUpdateCmd)).thenReturn(companyUpdateVO);
    Mockito.when(service.updateCompany(companyUpdateVO)).thenThrow(new CompanyNotFoundException("No company found with that ID."));

    Assertions.assertThrows(CompanyNotFoundException.class, () -> handler.updateCompany(companyUpdateCmd));
    Mockito.verify(mapper, Mockito.times(1)).mapToCompanyUpdateVO(companyUpdateCmd);
    Mockito.verify(service, Mockito.times(1)).updateCompany(companyUpdateVO);

  }
}