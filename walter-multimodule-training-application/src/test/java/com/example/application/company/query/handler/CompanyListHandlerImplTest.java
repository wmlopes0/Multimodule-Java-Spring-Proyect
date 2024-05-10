package com.example.application.company.query.handler;

import java.util.List;
import java.util.stream.Stream;

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
class CompanyListHandlerImplTest {

  @Mock
  private CompanyService service;

  @InjectMocks
  private CompanyListHandlerImpl handler;

  @ParameterizedTest
  @MethodSource("listCompaniesParameters")
  @DisplayName("Retrieve Company list successfully")
  void listCompaniesTest(List<Company> companies) {
    Mockito.when(service.getCompanies()).thenReturn(companies);

    List<Company> result = handler.listCompanies();
    Assertions.assertEquals(companies, result);

    Mockito.verify(service, Mockito.times(1)).getCompanies();
  }

  private static Stream<Arguments> listCompaniesParameters() {
    return Stream.of(
        Arguments.of(
            List.of(new Company()
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
                    )),
                new Company()
                    .setCif("B86017472")
                    .setName("Company2 S.L")
                    .setEmployees(List.of(
                        new Employee()
                            .setNif("27748713H")
                            .setName("Manolo")
                            .setSurname("Martín Lopes")
                            .setBirthYear(1998)
                            .setGender(Gender.MALE)
                            .setCompanyPhone("+34676615106")
                            .setPersonalPhone("+34722748406")
                            .setCompany("B86017472")
                            .setEmail("manolo@gmail.com"),
                        new Employee()
                            .setNif("83765493E")
                            .setName("Maria")
                            .setSurname("Barbero Sánchez")
                            .setBirthYear(1996)
                            .setGender(Gender.FEMALE)
                            .setPersonalPhone("+34676615106")
                            .setCompany("B86017472")
                            .setEmail("maria@gmail.com")
                    )))
        )
    );
  }

  @Test
  @DisplayName("Retrieve empty Company list when no companies present")
  void emptyListCompaniesTest() {
    List<Company> expected = List.of();
    Mockito.when(service.getCompanies()).thenReturn(List.of());

    List<Company> result = handler.listCompanies();
    Assertions.assertEquals(expected, result);

    Mockito.verify(service, Mockito.times(1)).getCompanies();
  }
}