package com.example.contract.company.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.example.boot.app.App;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
import com.example.infrastructure.entity.CompanyEntity;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.entity.PhoneEntity;
import com.example.infrastructure.repository.CompanyRepository;
import com.example.infrastructure.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = App.class, properties = {"spring.profiles.active = test"})
@AutoConfigureMockMvc
class CompanyRestControllerE2ETestIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @BeforeEach
  void init() {
    companyRepository.deleteAll();
    employeeRepository.deleteAll();
  }

  @ParameterizedTest
  @MethodSource("listCompaniesParameters")
  @DisplayName("List companies return correctly list")
  void listCompaniesTest(CompanyEntity companyEntity1, CompanyEntity companyEntity2, String expected) throws Exception {
    companyRepository.save(companyEntity1);
    companyRepository.save(companyEntity2);
    mockMvc.perform(get("/companies/"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, false));
  }

  private static Stream<Arguments> listCompaniesParameters() {
    return Stream.of(
        Arguments.of(
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company1 S.L"),
            new CompanyEntity()
                .setCif("U52304771")
                .setName("Company2 S.L"),
            """
                [
                  {
                    "cif": "B86017472",
                    "name": "Company1 S.L",
                    "employees": []
                  },
                  {
                    "cif": "U52304771",
                    "name": "Company2 S.L",
                    "employees": []
                  }
                ]"""
        )
    );
  }

  @Test
  @DisplayName("When list companies is empty return correctly list")
  void listCompaniesEmptyTest() throws Exception {
    String expected = """
        []""";

    mockMvc.perform(get("/companies/"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, true));
  }

  @ParameterizedTest
  @MethodSource("getCompanyByIdParameters")
  @DisplayName("Get company by CIF returns company and 200 response correctly")
  void getCompanyByIdTest(String cif, CompanyEntity companyEntity, String expected) throws Exception {
    employeeRepository.save(new EmployeeEntity()
        .setNif("27748713H")
        .setName("Manolo")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
        .setCompany(cif)
        .setEmail("manolo@gmail.com"));
    companyRepository.save(companyEntity);
    mockMvc.perform(get("/companies/{cif}", cif))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, false));
  }

  private static Stream<Arguments> getCompanyByIdParameters() {
    return Stream.of(
        Arguments.of(
            "B86017472",
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company1 S.L"),
            """
                {
                    "cif": "B86017472",
                    "name": "Company1 S.L",
                    "employees": [
                      {
                        "nif": "27748713H",
                        "name": "Manolo",
                        "surname":"Martín Lopes",
                        "birthYear": 1998,
                        "gender": "MALE",
                        "personalPhone":"+34722748406",
                        "company": "B86017472",
                        "email": "manolo@gmail.com"
                      }
                    ]
                  }"""
        )
    );
  }

  @Test
  @DisplayName("Get company by CIF not found returns 404 response")
  void getCompanyByIdNotFoundTest() throws Exception {
    String expected = "";
    String cif = "B86017472";

    mockMvc.perform(get("/companies/{cif}", cif))
        .andExpect(status().isNotFound())
        .andExpect(content().string(expected));
  }

  @ParameterizedTest
  @MethodSource("newCompanyParameters")
  @DisplayName("Add new company returns 201 response")
  void newCompanyTest(String cif, String jsonContent, String expected, CompanyEntity entityExpected) throws Exception {
    mockMvc.perform(post("/companies/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isCreated())
        .andExpect(content().json(expected, false));

    Optional<CompanyEntity> fetchExpected = Optional.of(entityExpected);
    Optional<CompanyEntity> fetchResult = companyRepository.findById(cif);

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  private static Stream<Arguments> newCompanyParameters() {
    return Stream.of(
        Arguments.of(
            "B86017472",
            """
                {
                  "cif": "B86017472",
                  "name": "Company1 S.L"
                }
                """,
            """
                {
                  "cif": "B86017472",
                  "name": "Company1 S.L",
                  "employees": []
                }
                """,
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company1 S.L")
        )
    );
  }

  @ParameterizedTest
  @MethodSource("updateCompanyByIdParameters")
  @DisplayName("Update company by CIF successfully returns 200 code response")
  void updateCompanyByIdTest(String cif, EmployeeEntity employee1, EmployeeEntity employee2, CompanyEntity companyEntity,
      String jsonContent, String expected,
      CompanyEntity entityExpected) throws Exception {
    employeeRepository.save(employee1);
    employeeRepository.save(employee2);
    companyRepository.save(companyEntity);

    mockMvc.perform(put("/companies/{cif}", cif)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, false));

    Optional<CompanyEntity> fetchExpected = Optional.of(entityExpected);
    Optional<CompanyEntity> fetchResult = companyRepository.findById(cif);

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  private static Stream<Arguments> updateCompanyByIdParameters() {
    return Stream.of(
        Arguments.of(
            "B86017472",
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setCompany("B86017472")
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeEntity()
                .setNif("45132337N")
                .setName("Raquel")
                .setLastName("Barbero Sánchez")
                .setBirthYear(1996)
                .setCompany("B86017472")
                .setGender(Gender.FEMALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "676615106", PhoneType.PERSONAL)))
                .setEmail("raquelbarberosanchez90@gmail.com"),
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company1 S.L"),
            """
                {
                  "cif": "B86017472",
                  "name": "Company Name Changed"
                }
                """,
            """
                {
                  "cif": "B86017472",
                  "name": "Company Name Changed",
                  "employees": [
                    {
                    "nif": "45134320V",
                    "name": "Walter",
                    "surname":"Martín Lopes",
                    "birthYear": 1998,
                    "gender": "MALE",
                    "personalPhone":"+34722748406",
                    "company": "B86017472",
                    "email": "wmlopes0@gmail.com"
                    },
                    {
                    "nif": "45132337N",
                    "name": "Raquel",
                    "surname":"Barbero Sánchez",
                    "birthYear": 1996,
                    "gender": "FEMALE",
                    "personalPhone":"+34676615106",
                    "email": "raquelbarberosanchez90@gmail.com"
                    }
                  ]
                }
                """,
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company Name Changed")
        )
    );
  }

  @Test
  @DisplayName("Update company by CIF not found returns 404 code response")
  void updateCompanyByIdNotFoundTest() throws Exception {
    String cif = "B86017472";
    String jsonContent = """
        {
          "cif": "B86017472",
          "name": "Company Name Changed"
        }
        """;
    String expected = """
        {
          "error":"Internal Server Error",
          "message":"No company found with that ID."
        }
        """;
    mockMvc.perform(put("/companies/{cif}", cif)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isInternalServerError())
        .andExpect(content().json(expected, false));
  }

  @ParameterizedTest
  @MethodSource("deleteCompanyByIdParameters")
  @DisplayName("Deleted employee by CIF successfully returns 200 code response")
  void deleteCompanyByIdTest(CompanyEntity company, EmployeeEntity employee1, EmployeeEntity employee2) throws Exception {
    employeeRepository.save(employee1);
    employeeRepository.save(employee2);
    companyRepository.save(company);
    String cif = "B86017472";

    mockMvc.perform(delete("/companies/{cif}", cif))
        .andExpect(status().isOk());

    Optional<CompanyEntity> fetchExpected = Optional.empty();
    Optional<CompanyEntity> fetchResult = companyRepository.findById(cif);

    Assertions.assertEquals(fetchExpected, fetchResult);
    Assertions.assertNull(employeeRepository.findById(employee1.getNif()).get().getCompany());
    Assertions.assertNull(employeeRepository.findById(employee2.getNif()).get().getCompany());
  }

  private static Stream<Arguments> deleteCompanyByIdParameters() {
    return Stream.of(
        Arguments.of(
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company"),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setCompany("B86017472")
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeEntity()
                .setNif("45132337N")
                .setName("Raquel")
                .setLastName("Barbero Sánchez")
                .setBirthYear(1996)
                .setCompany("B86017472")
                .setGender(Gender.FEMALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "676615106", PhoneType.PERSONAL)))
                .setEmail("raquelbarberosanchez90@gmail.com")
        )
    );
  }

  @Test
  @DisplayName("Deleted employee by CIF failed returns 404 code response.")
  void deleteCompanyByIdNotFoundTest() throws Exception {
    String cif = "B86017472";
    mockMvc.perform(delete("/companies/{cif}", cif))
        .andExpect(status().isNotFound());
  }
}
