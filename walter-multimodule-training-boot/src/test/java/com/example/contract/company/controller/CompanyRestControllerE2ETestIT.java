package com.example.contract.company.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.stream.Stream;

import com.example.boot.app.App;
import com.example.infrastructure.entity.CompanyEntity;
import com.example.infrastructure.repository.CompanyRepository;
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

  @BeforeEach
  void init() {
    companyRepository.deleteAll();
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
                    "employees": []
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
  void updateCompanyByIdTest(String cif, CompanyEntity companyEntity, String jsonContent, String expected,
      CompanyEntity entityExpected) throws Exception {
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
                  "employees": []
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

  @Test
  @DisplayName("Deleted employee by CIF successfully returns 200 code response")
  void deleteCompanyByIdTest() throws Exception {
    String cif = "B86017472";
    companyRepository.save(new CompanyEntity()
        .setCif("B86017472")
        .setName("Company"));

    mockMvc.perform(delete("/companies/{cif}", cif))
        .andExpect(status().isOk());

    Optional<CompanyEntity> fetchExpected = Optional.empty();
    Optional<CompanyEntity> fetchResult = companyRepository.findById(cif);

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @Test
  @DisplayName("Deleted employee by CIF failed returns 404 code response.")
  void deleteCompanyByIdNotFoundTest() throws Exception {
    String cif = "B86017472";
    mockMvc.perform(delete("/companies/{cif}", cif))
        .andExpect(status().isNotFound());
  }
}
