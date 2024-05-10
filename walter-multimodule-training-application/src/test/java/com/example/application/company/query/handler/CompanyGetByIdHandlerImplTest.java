package com.example.application.company.query.handler;

import java.util.List;

import com.example.application.company.query.dto.CompanyByIdQuery;
import com.example.domain.entity.Company;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
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
class CompanyGetByIdHandlerImplTest {

  @Mock
  private CompanyService service;

  @InjectMocks
  private CompanyGetByIdHandlerImpl handler;

  @Test
  @DisplayName("Retrieve Company by CIF successfully")
  void getCompanyByIdTest() {
    CompanyByIdQuery companyByIdQuery = new CompanyByIdQuery("Q4947066I");
    Company company = new Company()
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
                .setEmail("wmlopes0@gmail.com")));

    Mockito.when(service.getCompany(companyByIdQuery.getCif())).thenReturn(company);
    Company result = handler.getCompanyById(companyByIdQuery);

    Assertions.assertEquals(company, result);
    Mockito.verify(service, Mockito.times(1)).getCompany(companyByIdQuery.getCif());
  }

  @Test
  @DisplayName("Company Not Found by CIF")
  void getCompanyByIdNotFoundTest() {
    CompanyByIdQuery companyByIdQuery = new CompanyByIdQuery("Q4947066I");

    Mockito.when(service.getCompany(companyByIdQuery.getCif())).thenReturn(null);
    Company result = handler.getCompanyById(companyByIdQuery);

    Assertions.assertNull(result);
    Mockito.verify(service, Mockito.times(1)).getCompany(companyByIdQuery.getCif());
  }
}