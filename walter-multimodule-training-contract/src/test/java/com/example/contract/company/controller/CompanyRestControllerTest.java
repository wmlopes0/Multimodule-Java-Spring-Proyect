package com.example.contract.company.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.application.company.cmd.dto.CompanyDeleteCmd;
import com.example.application.company.cmd.dto.CompanyUpdateCmd;
import com.example.application.company.cmd.handler.CompanyCreateHandler;
import com.example.application.company.cmd.handler.CompanyDeleteHandler;
import com.example.application.company.cmd.handler.CompanyUpdateHandler;
import com.example.application.company.query.dto.CompanyByIdQuery;
import com.example.application.company.query.handler.CompanyGetByIdHandler;
import com.example.application.company.query.handler.CompanyListHandler;
import com.example.contract.company.dto.CompanyRequestDTO;
import com.example.contract.company.dto.CompanyResponseDTO;
import com.example.contract.company.dto.CompanyUpdateDTO;
import com.example.contract.company.dto.EmployeeDTO;
import com.example.contract.company.mapper.CompanyContractMapper;
import com.example.domain.entity.Company;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CompanyRestControllerTest {

  @Mock
  private CompanyCreateHandler companyCreateHandler;

  @Mock
  private CompanyDeleteHandler companyDeleteHandler;

  @Mock
  private CompanyUpdateHandler companyUpdateHandler;

  @Mock
  private CompanyGetByIdHandler companyGetByIdHandler;

  @Mock
  private CompanyListHandler companyListHandler;

  @Mock
  private CompanyContractMapper mapper;

  @InjectMocks
  private CompanyRestController controller;

  @ParameterizedTest
  @MethodSource("listCompaniesParameters")
  @DisplayName("List companies return correctly list")
  void listCompaniesTest(Company company1, Company company2, CompanyResponseDTO companyResponse1, CompanyResponseDTO companyResponse2) {
    Mockito.when(companyListHandler.listCompanies()).thenReturn(List.of(company1, company2));
    Mockito.when(mapper.mapToCompanyResponseDTO(company1)).thenReturn(companyResponse1);
    Mockito.when(mapper.mapToCompanyResponseDTO(company2)).thenReturn(companyResponse2);

    ResponseEntity<List<CompanyResponseDTO>> result = controller.listCompanies();
    ResponseEntity<List<CompanyResponseDTO>> expected = ResponseEntity.ok(List.of(companyResponse1, companyResponse2));

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(companyListHandler, times(1)).listCompanies();
    Mockito.verify(mapper, atLeastOnce()).mapToCompanyResponseDTO(any(Company.class));
  }

  private static Stream<Arguments> listCompaniesParameters() {
    return Stream.of(
        Arguments.of(
            new Company()
                .setCif("V33778580")
                .setName("Company S.L")
                .setEmployees(List.of(
                    new Employee()
                        .setNif("45134320V")
                        .setName("Walter")
                        .setSurname("Martín Lopes")
                        .setBirthYear(1998)
                        .setGender(Gender.MALE)
                        .setPersonalPhone("+34722748406")
                        .setCompanyPhone("+34676615106")
                        .setEmail("wmlopes0@gmail.com"),
                    new Employee()
                        .setNif("45132337N")
                        .setName("Raquel")
                        .setSurname("Barbero Sánchez")
                        .setBirthYear(1996)
                        .setGender(Gender.FEMALE)
                        .setPersonalPhone("+34676615106")
                        .setCompanyPhone("+34722748406")
                        .setEmail("raquelbarberosanchez90@gmail.com")
                )),
            new Company()
                .setCif("V33778581")
                .setName("Company2 S.L")
                .setEmployees(new ArrayList<>()),
            new CompanyResponseDTO()
                .setCif("V33778580")
                .setName("Company S.L")
                .setEmployees(List.of(
                    new EmployeeDTO()
                        .setNif("45134320V")
                        .setName("Walter")
                        .setSurname("Martín Lopes")
                        .setBirthYear(1998)
                        .setGender(Gender.MALE.name())
                        .setPersonalPhone("+34722748406")
                        .setCompanyPhone("+34676615106")
                        .setEmail("wmlopes0@gmail.com"),
                    new EmployeeDTO()
                        .setNif("45132337N")
                        .setName("Raquel")
                        .setSurname("Barbero Sánchez")
                        .setBirthYear(1996)
                        .setGender(Gender.FEMALE.name())
                        .setPersonalPhone("+34676615106")
                        .setCompanyPhone("+34722748406")
                        .setEmail("raquelbarberosanchez90@gmail.com")
                )),
            new CompanyResponseDTO()
                .setCif("V33778581")
                .setName("Company2 S.L")
                .setEmployees(new ArrayList<>())
        )
    );
  }

  @Test
  @DisplayName("When list companies is empty return correctly list")
  void listCompaniesEmptyTest() {
    Mockito.when(companyListHandler.listCompanies()).thenReturn(List.of());

    ResponseEntity<List<CompanyResponseDTO>> result = controller.listCompanies();
    ResponseEntity<List<CompanyResponseDTO>> expected = ResponseEntity.ok(List.of());

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(companyListHandler, times(1)).listCompanies();
    Mockito.verify(mapper, never()).mapToCompanyResponseDTO(any(Company.class));
  }

  @ParameterizedTest
  @MethodSource("getCompanyByIdParameters")
  @DisplayName("Get company by CIF returns company and 200 response correctly")
  void getCompanyByIdTest(Company company, CompanyResponseDTO companyResponseDTO) {
    String cif = "V33778580";
    CompanyByIdQuery companyByIdQuery = new CompanyByIdQuery(cif);

    Mockito.when(mapper.mapToCompanyByIdQuery(cif)).thenReturn(companyByIdQuery);
    Mockito.when(companyGetByIdHandler.getCompanyById(companyByIdQuery)).thenReturn(company);
    Mockito.when(mapper.mapToCompanyResponseDTO(company)).thenReturn(companyResponseDTO);

    ResponseEntity<CompanyResponseDTO> result = controller.getCompanyById(cif);
    ResponseEntity<CompanyResponseDTO> expected = ResponseEntity.ok(companyResponseDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    Mockito.verify(mapper, times(1)).mapToCompanyByIdQuery(cif);
    Mockito.verify(companyGetByIdHandler, times(1)).getCompanyById(companyByIdQuery);
    Mockito.verify(mapper, times(1)).mapToCompanyResponseDTO(company);
  }

  private static Stream<Arguments> getCompanyByIdParameters() {
    return Stream.of(
        Arguments.of(
            new Company()
                .setCif("V33778580")
                .setName("Company S.L")
                .setEmployees(List.of(
                    new Employee()
                        .setNif("45134320V")
                        .setName("Walter")
                        .setSurname("Martín Lopes")
                        .setBirthYear(1998)
                        .setGender(Gender.MALE)
                        .setPersonalPhone("+34722748406")
                        .setCompanyPhone("+34676615106")
                        .setEmail("wmlopes0@gmail.com"),
                    new Employee()
                        .setNif("45132337N")
                        .setName("Raquel")
                        .setSurname("Barbero Sánchez")
                        .setBirthYear(1996)
                        .setGender(Gender.FEMALE)
                        .setPersonalPhone("+34676615106")
                        .setCompanyPhone("+34722748406")
                        .setEmail("raquelbarberosanchez90@gmail.com")
                )),
            new CompanyResponseDTO()
                .setCif("V33778580")
                .setName("Company S.L")
                .setEmployees(List.of(
                    new EmployeeDTO()
                        .setNif("45134320V")
                        .setName("Walter")
                        .setSurname("Martín Lopes")
                        .setBirthYear(1998)
                        .setGender(Gender.MALE.name())
                        .setPersonalPhone("+34722748406")
                        .setCompanyPhone("+34676615106")
                        .setEmail("wmlopes0@gmail.com"),
                    new EmployeeDTO()
                        .setNif("45132337N")
                        .setName("Raquel")
                        .setSurname("Barbero Sánchez")
                        .setBirthYear(1996)
                        .setGender(Gender.FEMALE.name())
                        .setPersonalPhone("+34676615106")
                        .setCompanyPhone("+34722748406")
                        .setEmail("raquelbarberosanchez90@gmail.com")
                ))
        )
    );
  }

  @Test
  @DisplayName("Get company by CIF not found returns 404 response")
  void getCompanyByIdNotFoundTest() {
    String cif = "V33778580";
    CompanyByIdQuery companyByIdQuery = new CompanyByIdQuery(cif);

    Mockito.when(mapper.mapToCompanyByIdQuery(cif)).thenReturn(companyByIdQuery);
    Mockito.when(companyGetByIdHandler.getCompanyById(companyByIdQuery)).thenReturn(null);

    ResponseEntity<CompanyResponseDTO> result = controller.getCompanyById(cif);
    ResponseEntity<CompanyResponseDTO> expected = ResponseEntity.notFound().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(mapper, times(1)).mapToCompanyByIdQuery(cif);
    Mockito.verify(companyGetByIdHandler, times(1)).getCompanyById(companyByIdQuery);
    Mockito.verify(mapper, never()).mapToCompanyResponseDTO(any(Company.class));
  }

  @ParameterizedTest
  @MethodSource("newCompanyParameters")
  @DisplayName("Add new company returns 201 response")
  void newCompanyTest(CompanyRequestDTO companyRequest, CompanyCreateCmd companyCreateCmd, Company company,
      CompanyResponseDTO companyResponseDTO) {

    Mockito.when(mapper.mapToCompanyCreateCmd(companyRequest)).thenReturn(companyCreateCmd);
    Mockito.when(companyCreateHandler.addCompany(companyCreateCmd)).thenReturn(company);
    Mockito.when(mapper.mapToCompanyResponseDTO(company)).thenReturn(companyResponseDTO);

    ResponseEntity<CompanyResponseDTO> result = controller.newCompany(companyRequest);
    ResponseEntity<CompanyResponseDTO> expected = ResponseEntity.status(HttpStatus.CREATED).body(companyResponseDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(mapper, times(1)).mapToCompanyCreateCmd(companyRequest);
    Mockito.verify(companyCreateHandler, times(1)).addCompany(companyCreateCmd);
    Mockito.verify(mapper, times(1)).mapToCompanyResponseDTO(company);
  }

  private static Stream<Arguments> newCompanyParameters() {
    return Stream.of(
        Arguments.of(
            new CompanyRequestDTO()
                .setCif("V33778580")
                .setName("Company S.L"),
            new CompanyCreateCmd()
                .setCif("V33778580")
                .setName("Company S.L"),
            new Company()
                .setCif("V33778580")
                .setName("Company S.L")
                .setEmployees(new ArrayList<>()),
            new CompanyResponseDTO()
                .setCif("V33778580")
                .setName("Company S.L")
                .setEmployees(new ArrayList<>())
        )
    );
  }

  @ParameterizedTest
  @MethodSource("updateCompanyByIdParameters")
  @DisplayName("Update company by CIF successfully returns 200 code response")
  void updateCompanyByIdTest(String cif, CompanyUpdateDTO companyRequest, CompanyUpdateCmd companyUpdateCmd,
      Company company, CompanyResponseDTO companyResponseDTO) {
    Mockito.when(mapper.mapToCompanyUpdateCmd(cif, companyRequest)).thenReturn(companyUpdateCmd);
    Mockito.when(companyUpdateHandler.updateCompany(companyUpdateCmd)).thenReturn(company);
    Mockito.when(mapper.mapToCompanyResponseDTO(company)).thenReturn(companyResponseDTO);

    ResponseEntity<CompanyResponseDTO> result = controller.updateCompanyById(cif, companyRequest);
    ResponseEntity<CompanyResponseDTO> expected = ResponseEntity.status(HttpStatus.OK).body(companyResponseDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(mapper, times(1)).mapToCompanyUpdateCmd(cif, companyRequest);
    Mockito.verify(companyUpdateHandler, times(1)).updateCompany(companyUpdateCmd);
    Mockito.verify(mapper, times(1)).mapToCompanyResponseDTO(company);
  }

  private static Stream<Arguments> updateCompanyByIdParameters() {
    return Stream.of(
        Arguments.of(
            "V33778580",
            new CompanyUpdateDTO("Company S.L"),
            new CompanyUpdateCmd().setCif("V33778580").setName("Company S.L"),
            new Company()
                .setCif("V33778580")
                .setName("Company S.L")
                .setEmployees(new ArrayList<>()),
            new CompanyResponseDTO()
                .setCif("V33778580")
                .setName("Company S.L")
                .setEmployees(new ArrayList<>())
        )
    );
  }

  @Test
  @DisplayName("Update company by CIF not found returns 404 code response")
  void updateCompanyByIdNotFoundTest() {
    String cif = "V33778580";
    CompanyUpdateDTO companyUpdateDTO = new CompanyUpdateDTO("Company S.L");
    CompanyUpdateCmd companyUpdateCmd = new CompanyUpdateCmd()
        .setCif("V33778580")
        .setName("Company S.L");

    Mockito.when(mapper.mapToCompanyUpdateCmd(cif, companyUpdateDTO)).thenReturn(companyUpdateCmd);
    Mockito.when(companyUpdateHandler.updateCompany(companyUpdateCmd)).thenReturn(null);

    Exception exception = Assertions.assertThrows(ResponseStatusException.class, () -> controller.updateCompanyById(cif, companyUpdateDTO));
    Assertions.assertTrue(exception.getMessage().contains("Company not found."));
    Mockito.verify(mapper, times(1)).mapToCompanyUpdateCmd(cif, companyUpdateDTO);
    Mockito.verify(companyUpdateHandler, times(1)).updateCompany(companyUpdateCmd);
    Mockito.verify(mapper, never()).mapToCompanyResponseDTO(any(Company.class));
  }

  @Test
  @DisplayName("Deleted employee by CIF successfully returns 200 code response")
  void deleteCompanyByIdTest() {
    String cif = "V33778580";
    CompanyDeleteCmd companyDeleteCmd = new CompanyDeleteCmd(cif);

    Mockito.when(mapper.mapToCompanyDeleteCmd(cif)).thenReturn(companyDeleteCmd);
    Mockito.when(companyDeleteHandler.deleteCompany(companyDeleteCmd)).thenReturn(true);

    ResponseEntity<Object> result = controller.deleteCompanyById(cif);
    ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.OK).build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Mockito.verify(mapper, times(1)).mapToCompanyDeleteCmd(cif);
    Mockito.verify(companyDeleteHandler, times(1)).deleteCompany(companyDeleteCmd);
  }

  @Test
  @DisplayName("Deleted employee by CIF failed returns 404 code response.")
  void deleteCompanyByIdNotFoundTest() {
    String cif = "V33778580";
    CompanyDeleteCmd companyDeleteCmd = new CompanyDeleteCmd(cif);

    Mockito.when(mapper.mapToCompanyDeleteCmd(cif)).thenReturn(companyDeleteCmd);
    Mockito.when(companyDeleteHandler.deleteCompany(companyDeleteCmd)).thenReturn(false);

    ResponseEntity<Object> result = controller.deleteCompanyById(cif);
    ResponseEntity<Object> expected = ResponseEntity.notFound().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Mockito.verify(mapper, times(1)).mapToCompanyDeleteCmd(cif);
    Mockito.verify(companyDeleteHandler, times(1)).deleteCompany(companyDeleteCmd);
  }
}