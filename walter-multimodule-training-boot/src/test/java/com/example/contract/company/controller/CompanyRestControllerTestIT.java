package com.example.contract.company.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.example.boot.app.App;
import com.example.contract.company.dto.CompanyRequestDTO;
import com.example.contract.company.dto.CompanyResponseDTO;
import com.example.contract.company.dto.CompanyUpdateDTO;
import com.example.contract.company.dto.EmployeeDTO;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
import com.example.domain.exception.CompanyNotFoundException;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(classes = App.class, properties = {"spring.profiles.active = test"})
class CompanyRestControllerTestIT {

  @Autowired
  private CompanyRestController controller;

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
  void listCompaniesTest(CompanyEntity companyEntity1, CompanyEntity companyEntity2, List<CompanyResponseDTO> listCompanies) {
    companyRepository.save(companyEntity1);
    companyRepository.save(companyEntity2);

    ResponseEntity<List<CompanyResponseDTO>> expected = ResponseEntity.ok(listCompanies);
    ResponseEntity<List<CompanyResponseDTO>> result = controller.listCompanies();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
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
            List.of(
                new CompanyResponseDTO()
                    .setCif("B86017472")
                    .setName("Company1 S.L")
                    .setEmployees(List.of()
                    ),
                new CompanyResponseDTO()
                    .setCif("U52304771")
                    .setName("Company2 S.L")
                    .setEmployees(List.of())
            )
        )
    );
  }

  @Test
  @DisplayName("When list companies is empty return correctly list")
  void listCompaniesEmptyTest() {
    ResponseEntity<List<CompanyResponseDTO>> expected = ResponseEntity.ok(List.of());
    ResponseEntity<List<CompanyResponseDTO>> result = controller.listCompanies();
    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @ParameterizedTest
  @MethodSource("getCompanyByIdParameters")
  @DisplayName("Get company by CIF returns company and 200 response correctly")
  void getCompanyByIdTest(String cif, CompanyEntity companyEntity, CompanyResponseDTO companyResponseDTO) {
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
    ResponseEntity<CompanyResponseDTO> expected = ResponseEntity.ok(companyResponseDTO);
    ResponseEntity<CompanyResponseDTO> result = controller.getCompanyById(cif);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  private static Stream<Arguments> getCompanyByIdParameters() {
    return Stream.of(
        Arguments.of(
            "B86017472",
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company1 S.L"),
            new CompanyResponseDTO()
                .setCif("B86017472")
                .setName("Company1 S.L")
                .setEmployees(List.of(
                        new EmployeeDTO()
                            .setNif("27748713H")
                            .setName("Manolo")
                            .setSurname("Martín Lopes")
                            .setBirthYear(1998)
                            .setGender("MALE")
                            .setPersonalPhone("+34722748406")
                            .setCompany("B86017472")
                            .setEmail("manolo@gmail.com")
                    )
                )
        )
    );
  }

  @Test
  @DisplayName("Get company by CIF not found returns 404 response")
  void getCompanyByIdNotFoundTest() {
    ResponseEntity<CompanyResponseDTO> expected = ResponseEntity.notFound().build();
    ResponseEntity<CompanyResponseDTO> result = controller.getCompanyById("U52304771");

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @ParameterizedTest
  @MethodSource("newCompanyParameters")
  @DisplayName("Add new company returns 201 response")
  void newCompanyTest(CompanyRequestDTO companyRequestDTO, CompanyResponseDTO companyResponseDTO, CompanyEntity companyEntity) {
    ResponseEntity<CompanyResponseDTO> result = controller.newCompany(companyRequestDTO);
    ResponseEntity<CompanyResponseDTO> expected =
        ResponseEntity.status(HttpStatus.CREATED).body(companyResponseDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    Optional<CompanyEntity> fetchExpected = Optional.of(companyEntity);
    Optional<CompanyEntity> fetchResult = companyRepository.findById(companyRequestDTO.getCif());

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  private static Stream<Arguments> newCompanyParameters() {
    return Stream.of(
        Arguments.of(
            new CompanyRequestDTO()
                .setCif("U52304771")
                .setName("Company2 S.L"),
            new CompanyResponseDTO()
                .setCif("U52304771")
                .setName("Company2 S.L")
                .setEmployees(List.of()),
            new CompanyEntity()
                .setCif("U52304771")
                .setName("Company2 S.L")
        )
    );
  }

  @ParameterizedTest
  @MethodSource("updateCompanyByIdParameters")
  @DisplayName("Update company by CIF successfully returns 200 code response")
  void updateCompanyByIdTest(CompanyEntity companyEntity, String cif, CompanyUpdateDTO companyUpdateDTO,
      CompanyResponseDTO companyResponseDTO, CompanyEntity entityExpected) {
    companyRepository.save(companyEntity);

    ResponseEntity<CompanyResponseDTO> expected = ResponseEntity.ok(companyResponseDTO);
    ResponseEntity<CompanyResponseDTO> result = controller.updateCompanyById(cif, companyUpdateDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    Optional<CompanyEntity> fetchExpected = Optional.of(entityExpected);
    Optional<CompanyEntity> fetchResult = companyRepository.findById(cif);

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  private static Stream<Arguments> updateCompanyByIdParameters() {
    return Stream.of(
        Arguments.of(
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company1 S.L"),
            "B86017472",
            new CompanyUpdateDTO()
                .setName("CompanyNameChanged"),
            new CompanyResponseDTO()
                .setCif("B86017472")
                .setName("CompanyNameChanged")
                .setEmployees(List.of()),
            new CompanyEntity()
                .setCif("B86017472")
                .setName("CompanyNameChanged")
        )
    );
  }

  @Test
  @DisplayName("Update company by CIF not found returns 404 code response")
  void updateCompanyByIdNotFoundTest() {
    CompanyUpdateDTO companyUpdateDTO = new CompanyUpdateDTO("CompanyNameChanged");
    String expectedMessage = "No company found with that ID.";
    Exception exception = Assertions.assertThrows(CompanyNotFoundException.class,
        () -> controller.updateCompanyById("B86017472", companyUpdateDTO));

    Assertions.assertEquals(expectedMessage, exception.getMessage());
  }

  @ParameterizedTest
  @MethodSource("deleteCompanyByIdTest")
  @DisplayName("Deleted employee by CIF successfully returns 200 code response")
  void deleteCompanyByIdTest(String cif, CompanyEntity companyEntity) {
    companyRepository.save(companyEntity);

    ResponseEntity<Object> deleteExpected = ResponseEntity.ok().build();
    ResponseEntity<Object> deleteResult = controller.deleteCompanyById(cif);

    Assertions.assertEquals(deleteExpected.getStatusCode(), deleteResult.getStatusCode());

    Optional<CompanyEntity> fetchResult = companyRepository.findById(cif);
    Assertions.assertTrue(fetchResult.isEmpty());
  }

  private static Stream<Arguments> deleteCompanyByIdTest() {
    return Stream.of(
        Arguments.of(
            "B86017472",
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company1 S.L")
        )
    );
  }

  @Test
  @DisplayName("Deleted employee by CIF failed returns 404 code response.")
  void deleteCompanyByIdNotFoundTest() {
    ResponseEntity<Object> expected = ResponseEntity.notFound().build();
    ResponseEntity<Object> result = controller.deleteCompanyById("B86017472");

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

}
