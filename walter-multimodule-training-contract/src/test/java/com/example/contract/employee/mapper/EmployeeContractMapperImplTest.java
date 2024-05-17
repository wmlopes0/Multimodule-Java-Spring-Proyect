package com.example.contract.employee.mapper;

import java.util.List;
import java.util.stream.Stream;

import com.example.application.employee.cmd.dto.AddEmployeeToCompanyCmd;
import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.cmd.dto.RemoveEmployeeFromCompanyCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
import org.example.rest.model.CompanyDTO;
import org.example.rest.model.EmployeeRequestDTO;
import org.example.rest.model.EmployeeResponseDTO;
import org.example.rest.model.EmployeeUpdateDTO;
import org.example.rest.model.PhoneDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EmployeeContractMapperImplTest {

  private final EmployeeContractMapper mapper = new EmployeeContractMapperImpl();

  @Test
  @DisplayName("Mapping EmployeeRequestDTO to EmployeeCreateCmd correctly")
  void mapToEmployeeCreateCmdTest() {
    EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender("Male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");
    EmployeeCreateCmd expected = new EmployeeCreateCmd()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender("Male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");

    EmployeeCreateCmd result = mapper.mapToEmployeeCreateCmd(employeeRequestDTO);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping id to EmployeeDeleteCmd correctly")
  void mapToEmployeeDeleteCmdTest() {
    String nif = "45134320V";
    EmployeeDeleteCmd expected = new EmployeeDeleteCmd(nif);
    EmployeeDeleteCmd result = mapper.mapToEmployeeDeleteCmd(nif);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping id and EmployeeRequestDTO to EmployeeUpdateCmd correctly")
  void mapToEmployeeUpdateCmdTest() {
    String nif = "45134320V";
    EmployeeUpdateDTO employeeUpdateDTO = new EmployeeUpdateDTO()
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender("Male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");
    EmployeeUpdateCmd expected = new EmployeeUpdateCmd()
        .setNif(nif)
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender("Male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");

    EmployeeUpdateCmd result = mapper.mapToEmployeeUpdateCmd(nif, employeeUpdateDTO);
    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping EmployeeNifDTO to EmployeeByIdQuery correctly")
  void mapToEmployeeByIdQueryTest() {
    String nif = "45134320V";
    EmployeeByIdQuery expected = new EmployeeByIdQuery(nif);
    EmployeeByIdQuery result = mapper.mapToEmployeeByIdQuery(nif);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping EmployeeNameDTO to EmployeeByNameQuery correctly")
  void mapToEmployeeByNameQueryTest() {
    String name = "Walter";
    EmployeeByNameQuery expected = new EmployeeByNameQuery(name);
    EmployeeByNameQuery result = mapper.mapToEmployeeByNameQuery(name);

    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("mapToResponseDTOParameters")
  @DisplayName("Mapping Employee to EmployeeResponseDTO correctly")
  void mapToResponseDTOTest(Employee employee, EmployeeResponseDTO expected) {
    EmployeeResponseDTO result = mapper.mapToResponseDTO(employee);
    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> mapToResponseDTOParameters() {
    return Stream.of(
        Arguments.of(
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setPersonalPhone("+34722748406")
                .setCompanyPhone("+34676615106")
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45134320V")
                .setCompleteName("Martín Lopes, Walter")
                .setBirthYear(1998)
                .setAge(26)
                .setAdult(true)
                .setGender("Male")
                .setPhones(List.of(
                    new PhoneDTO().setNumber("+34722748406").setType(PhoneType.PERSONAL.name()),
                    new PhoneDTO().setNumber("+34676615106").setType(PhoneType.COMPANY.name())))
                .setEmail("wmlopes0@gmail.com")
        ),
        Arguments.of(
            new Employee()
                .setNif("45132337N")
                .setName("Raquel")
                .setSurname("Barbero Sánchez")
                .setBirthYear(2010)
                .setGender(Gender.FEMALE)
                .setPersonalPhone("+34722748406")
                .setEmail("raquelbarberosanchez90@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45132337N")
                .setCompleteName("Barbero Sánchez, Raquel")
                .setBirthYear(2010)
                .setAge(14)
                .setAdult(false)
                .setGender("Female")
                .setPhones(List.of(
                    new PhoneDTO().setNumber("+34722748406").setType(PhoneType.PERSONAL.name())))
                .setEmail("raquelbarberosanchez90@gmail.com")
        ),
        Arguments.of(
            new Employee()
                .setNif("45132337N")
                .setName("Raquel")
                .setSurname("Barbero Sánchez")
                .setBirthYear(2010)
                .setGender(Gender.FEMALE)
                .setCompanyPhone("+34722748406")
                .setEmail("raquelbarberosanchez90@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45132337N")
                .setCompleteName("Barbero Sánchez, Raquel")
                .setBirthYear(2010)
                .setAge(14)
                .setAdult(false)
                .setGender("Female")
                .setPhones(List.of(
                    new PhoneDTO().setNumber("+34722748406").setType(PhoneType.COMPANY.name())))
                .setEmail("raquelbarberosanchez90@gmail.com")
        )
    );
  }

  @Test
  @DisplayName("Mapping NIF and CompanyDTO to AddEmployeeToCompanyCmd correctly")
  void mapToAddEmployeeToCompanyCmdTest() {
    String nif = "45134320V";
    CompanyDTO companyDTO = new CompanyDTO("P5745796B");

    AddEmployeeToCompanyCmd expected = new AddEmployeeToCompanyCmd("45134320V", "P5745796B");
    AddEmployeeToCompanyCmd result = mapper.mapToAddEmployeeToCompanyCmd(nif, companyDTO);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping NIF and CompanyDTO from RemoveEmployeeToCompanyCmd correctly")
  void mapToRemoveEmployeeFromCompanyCmdTest() {
    String nif = "45134320V";
    CompanyDTO companyDTO = new CompanyDTO("P5745796B");

    RemoveEmployeeFromCompanyCmd expected = new RemoveEmployeeFromCompanyCmd("45134320V", "P5745796B");
    RemoveEmployeeFromCompanyCmd result = mapper.removeEmployeeFromCompanyCmd(nif, companyDTO);

    Assertions.assertEquals(expected, result);
  }
}