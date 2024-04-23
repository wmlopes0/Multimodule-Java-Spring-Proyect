package com.example.contract.company.mapper;

import java.util.List;
import java.util.stream.Stream;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.application.company.cmd.dto.CompanyDeleteCmd;
import com.example.application.company.cmd.dto.CompanyUpdateCmd;
import com.example.application.company.query.dto.CompanyByIdQuery;
import com.example.contract.company.dto.CompanyRequestDTO;
import com.example.contract.company.dto.CompanyResponseDTO;
import com.example.contract.company.dto.CompanyUpdateDTO;
import com.example.contract.company.dto.EmployeeDTO;
import com.example.domain.entity.Company;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CompanyContractMapperImplTest {

  private final CompanyContractMapper mapper = new CompanyContractMapperImpl();

  @Test
  @DisplayName("Mapping CompanyRequestDTO to CompanyCreateCmd correctly")
  void mapToCompanyCreateCmd() {
    CompanyRequestDTO companyRequestDTO = new CompanyRequestDTO()
        .setCif("V33778580")
        .setName("Company S.L");
    CompanyCreateCmd expected = new CompanyCreateCmd()
        .setCif("V33778580")
        .setName("Company S.L");
    CompanyCreateCmd result = mapper.mapToCompanyCreateCmd(companyRequestDTO);
    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping Cif to CompanyDeleteCmd correctly")
  void mapToCompanyDeleteCmd() {
    String cif = "V33778580";
    CompanyDeleteCmd expected = new CompanyDeleteCmd(cif);
    CompanyDeleteCmd result = mapper.mapToCompanyDeleteCmd(cif);
    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping Cif and CompanyUpdateDTO to CompanyUpdateCmd correctly")
  void mapToCompanyUpdateCmd() {
    String cif = "V33778580";
    CompanyUpdateDTO companyUpdateDTO = new CompanyUpdateDTO()
        .setName("Company S.L");
    CompanyUpdateCmd expected = new CompanyUpdateCmd()
        .setCif("V33778580")
        .setName("Company S.L");
    CompanyUpdateCmd result = mapper.mapToCompanyUpdateCmd(cif, companyUpdateDTO);
    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping Cif to CompanyByIdQuery correctly")
  void mapToCompanyByIdQuery() {
    String cif = "V33778580";
    CompanyByIdQuery expected = new CompanyByIdQuery()
        .setCif("V33778580");
    CompanyByIdQuery result = mapper.mapToCompanyByIdQuery(cif);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("listEmployeesParameters")
  @DisplayName("Mapping Company to CompanyResponseDTO correctly")
  void mapToCompanyResponseDTO(List<Employee> employees, List<EmployeeDTO> employeeDTOS) {
    Company company = new Company()
        .setCif("V33778580")
        .setName("Company S.L")
        .setEmployees(employees);
    CompanyResponseDTO expected = new CompanyResponseDTO()
        .setCif("V33778580")
        .setName("Company S.L")
        .setEmployees(employeeDTOS);
    CompanyResponseDTO result = mapper.mapToCompanyResponseDTO(company);
    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> listEmployeesParameters() {
    return Stream.of(
        Arguments.of(
            List.of(new Employee()
                    .setNif("45134320V")
                    .setName("Walter")
                    .setSurname("Martín Lopes")
                    .setBirthYear(1998)
                    .setGender(Gender.MALE)
                    .setCompanyPhone("+34676615106")
                    .setPersonalPhone("+34722748406")
                    .setEmail("wmlopes0@gmail.com"),
                new Employee()
                    .setNif("4513233YN")
                    .setName("Raquel")
                    .setSurname("Barbero Sánchez")
                    .setBirthYear(1996)
                    .setGender(Gender.FEMALE)
                    .setCompanyPhone("+34722748406")
                    .setPersonalPhone("+34676615106")
                    .setEmail("raquelbarberosanchez90@gmail.com")),
            List.of(new EmployeeDTO()
                    .setNif("45134320V")
                    .setName("Walter")
                    .setSurname("Martín Lopes")
                    .setBirthYear(1998)
                    .setGender(Gender.MALE.name())
                    .setCompanyPhone("+34676615106")
                    .setPersonalPhone("+34722748406")
                    .setEmail("wmlopes0@gmail.com"),
                new EmployeeDTO()
                    .setNif("4513233YN")
                    .setName("Raquel")
                    .setSurname("Barbero Sánchez")
                    .setBirthYear(1996)
                    .setGender(Gender.FEMALE.name())
                    .setCompanyPhone("+34722748406")
                    .setPersonalPhone("+34676615106")
                    .setEmail("raquelbarberosanchez90@gmail.com"))
        )
    );
  }

  @Test
  @DisplayName("Mapping Employee to EmployeeDTO correctly")
  void mapToEmployeeDTO() {
    Employee employee = new Employee()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE)
        .setCompanyPhone("+34676615106")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");
    EmployeeDTO expected = new EmployeeDTO()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.name())
        .setCompanyPhone("+34676615106")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");
    EmployeeDTO result = mapper.mapToEmployeeDTO(employee);
    Assertions.assertEquals(expected, result);
  }
}