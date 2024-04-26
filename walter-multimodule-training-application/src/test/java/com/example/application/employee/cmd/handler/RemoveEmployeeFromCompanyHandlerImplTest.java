package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.RemoveEmployeeFromCompanyCmd;
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
class RemoveEmployeeFromCompanyHandlerImplTest {

  @Mock
  private CompanyService repository;

  @InjectMocks
  private RemoveEmployeeFromCompanyHandlerImpl handler;

  @Test
  @DisplayName("Remove employee from company correctly")
  void removeEmployeeFromCompanyTest() {
    RemoveEmployeeFromCompanyCmd removeEmployeeFromCompanyCmd = new RemoveEmployeeFromCompanyCmd("45134320V", "Q4947066I");
    Mockito.when(repository.removeEmployeeFromCompany(removeEmployeeFromCompanyCmd.getNif(), removeEmployeeFromCompanyCmd.getCif()))
        .thenReturn(true);

    Assertions.assertTrue(handler.removeEmployeeFromCompany(removeEmployeeFromCompanyCmd));

    Mockito.verify(repository, Mockito.times(1))
        .removeEmployeeFromCompany(removeEmployeeFromCompanyCmd.getNif(), removeEmployeeFromCompanyCmd.getCif());
  }

  @Test
  @DisplayName("Remove employee from company not found return false")
  void removeEmployeeFromCompanyNotFoundTest() {
    RemoveEmployeeFromCompanyCmd removeEmployeeFromCompanyCmd = new RemoveEmployeeFromCompanyCmd("45134320V", "Q4947066I");
    Mockito.when(repository.removeEmployeeFromCompany(removeEmployeeFromCompanyCmd.getNif(), removeEmployeeFromCompanyCmd.getCif()))
        .thenReturn(false);

    Assertions.assertFalse(handler.removeEmployeeFromCompany(removeEmployeeFromCompanyCmd));

    Mockito.verify(repository, Mockito.times(1))
        .removeEmployeeFromCompany(removeEmployeeFromCompanyCmd.getNif(), removeEmployeeFromCompanyCmd.getCif());
  }
}