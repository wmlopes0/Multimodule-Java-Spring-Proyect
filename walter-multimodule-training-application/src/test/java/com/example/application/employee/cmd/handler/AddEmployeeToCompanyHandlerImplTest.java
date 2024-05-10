package com.example.application.employee.cmd.handler;

import java.util.List;
import java.util.stream.Stream;

import com.example.application.employee.cmd.dto.AddEmployeeToCompanyCmd;
import com.example.domain.entity.Company;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.service.CompanyService;
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
class AddEmployeeToCompanyHandlerImplTest {

  @Mock
  private CompanyService repository;

  @InjectMocks
  private AddEmployeeToCompanyHandlerImpl handler;

  @ParameterizedTest
  @MethodSource("addEmployeeToCompanyParameters")
  @DisplayName("Add Employee to Company correctly")
  void addEmployeeToCompanyTest(AddEmployeeToCompanyCmd addEmployeeToCompanyCmd, Company company) {
    Mockito.when(repository.addEmployeeToCompany(addEmployeeToCompanyCmd.getNif(), addEmployeeToCompanyCmd.getCif())).thenReturn(company);

    Company result = handler.addEmployeeToCompany(addEmployeeToCompanyCmd);
    Assertions.assertEquals(company, result);

    Mockito.verify(repository, Mockito.times(1)).addEmployeeToCompany(addEmployeeToCompanyCmd.getNif(), addEmployeeToCompanyCmd.getCif());
  }

  private static Stream<Arguments> addEmployeeToCompanyParameters() {
    return Stream.of(
        Arguments.of(
            new AddEmployeeToCompanyCmd("45134320V", "Q4947066I"),
            new Company()
                .setCif("Q4947066I")
                .setName("Company1 S.L")
                .setEmployees(List.of(
                    new Employee()
                        .setNif("45134320V")
                        .setName("Walter")
                        .setSurname("Martín Lopes")
                        .setBirthYear(1998)
                        .setGender(Gender.MALE)
                        .setCompanyPhone("+34676615106")
                        .setPersonalPhone("+34722748406")
                        .setCompany("Q4947066I")
                        .setEmail("wmlopes0@gmail.com"),
                    new Employee()
                        .setNif("45132337N")
                        .setName("Raquel")
                        .setSurname("Barbero Sánchez")
                        .setBirthYear(1996)
                        .setGender(Gender.FEMALE)
                        .setPersonalPhone("+34676615106")
                        .setCompany("Q4947066I")
                        .setEmail("raquelbarberosanchez90@gmail.com")
                ))
        )
    );
  }

  @Test
  @DisplayName("Add Employee to Company not found fails")
  void addEmployeeToCompanyNotFoundTest() {
    AddEmployeeToCompanyCmd addEmployeeToCompanyCmd = new AddEmployeeToCompanyCmd("45134320V", "Q4947066I");
    Mockito.when(repository.addEmployeeToCompany(addEmployeeToCompanyCmd.getNif(), addEmployeeToCompanyCmd.getCif())).thenReturn(null);

    Company result = handler.addEmployeeToCompany(addEmployeeToCompanyCmd);
    Assertions.assertNull(result);

    Mockito.verify(repository, Mockito.times(1)).addEmployeeToCompany(addEmployeeToCompanyCmd.getNif(), addEmployeeToCompanyCmd.getCif());
  }

}