package com.example.application.company.cmd.handler;

import com.example.application.company.cmd.dto.CompanyDeleteCmd;
import com.example.domain.service.CompanyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompanyDeleteHandlerImplTest {

  @Mock
  private CompanyService service;

  @InjectMocks
  private CompanyDeleteHandlerImpl handler;

  @Test
  @DisplayName("Delete Company by CIF successfully")
  void deleteCompanyTest() {
    CompanyDeleteCmd cmd = new CompanyDeleteCmd("Q4947066I");
    Mockito.when(service.deleteCompany(cmd.getCif())).thenReturn(true);

    Assertions.assertTrue(handler.deleteCompany(cmd));

    Mockito.verify(service, Mockito.times(1)).deleteCompany(cmd.getCif());
  }

  @Test
  @DisplayName("Delete Company by CIF fails for non-existent CIF")
  void deleteCompanyNotFoundTest() {
    CompanyDeleteCmd cmd = new CompanyDeleteCmd("Q4947066I");
    Mockito.when(service.deleteCompany(cmd.getCif())).thenReturn(false);

    Assertions.assertFalse(handler.deleteCompany(cmd));

    Mockito.verify(service, Mockito.times(1)).deleteCompany(cmd.getCif());
  }
}