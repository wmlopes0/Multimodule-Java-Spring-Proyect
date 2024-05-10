package com.example.contract.employee.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.stream.Stream;

import com.example.application.employee.cmd.dto.AddEmployeeToCompanyCmd;
import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.cmd.dto.RemoveEmployeeFromCompanyCmd;
import com.example.application.employee.cmd.handler.AddEmployeeToCompanyHandler;
import com.example.application.employee.cmd.handler.EmployeeCreateHandler;
import com.example.application.employee.cmd.handler.EmployeeDeleteHandler;
import com.example.application.employee.cmd.handler.EmployeeUpdateHandler;
import com.example.application.employee.cmd.handler.RemoveEmployeeFromCompanyHandler;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.application.employee.query.handler.EmployeeGetByIdHandler;
import com.example.application.employee.query.handler.EmployeeGetByNameHandler;
import com.example.application.employee.query.handler.EmployeeListHandler;
import com.example.contract.company.dto.CompanyResponseDTO;
import com.example.contract.company.dto.EmployeeDTO;
import com.example.contract.company.mapper.CompanyContractMapper;
import com.example.contract.employee.dto.CompanyDTO;
import com.example.contract.employee.dto.EmployeeRequestDTO;
import com.example.contract.employee.dto.EmployeeResponseDTO;
import com.example.contract.employee.dto.EmployeeUpdateDTO;
import com.example.contract.employee.dto.PhoneDTO;
import com.example.contract.employee.mapper.EmployeeContractMapper;
import com.example.domain.entity.Company;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class EmployeeRestControllerTest {

  @Mock
  private EmployeeCreateHandler employeeCreateHandler;

  @Mock
  private EmployeeDeleteHandler employeeDeleteHandler;

  @Mock
  private EmployeeUpdateHandler employeeUpdateHandler;

  @Mock
  private EmployeeGetByIdHandler employeeGetByIdHandler;

  @Mock
  private EmployeeGetByNameHandler employeeGetByNameHandler;

  @Mock
  private EmployeeListHandler employeeListHandler;

  @Mock
  private AddEmployeeToCompanyHandler addEmployeeToCompanyHandler;

  @Mock
  private RemoveEmployeeFromCompanyHandler removeEmployeeFromCompanyHandler;

  @Mock
  private EmployeeContractMapper mapper;

  @Mock
  private CompanyContractMapper companyMapper;

  @InjectMocks
  private EmployeeRestController controller;

  @ParameterizedTest
  @MethodSource("listEmployeesParameters")
  @DisplayName("List employees return correctly list")
  void listEmployeesTest(Employee employee1, Employee employee2, EmployeeResponseDTO employeeResponse1,
      EmployeeResponseDTO employeeResponse2) {

    Mockito.when(employeeListHandler.listEmployees()).thenReturn(List.of(employee1, employee2));
    Mockito.when(mapper.mapToResponseDTO(employee1)).thenReturn(employeeResponse1);
    Mockito.when(mapper.mapToResponseDTO(employee2)).thenReturn(employeeResponse2);

    ResponseEntity<List<EmployeeResponseDTO>> result = controller.listEmployees();
    ResponseEntity<List<EmployeeResponseDTO>> expected = ResponseEntity.ok(List.of(employeeResponse1, employeeResponse2));

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(employeeListHandler, times(1)).listEmployees();
    Mockito.verify(mapper, atLeastOnce()).mapToResponseDTO(any(Employee.class));
  }

  private static Stream<Arguments> listEmployeesParameters() {
    return Stream.of(
        Arguments.of(
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com"),
            new Employee()
                .setNif("45132337N")
                .setName("Raquel")
                .setSurname("Barbero Sánchez")
                .setBirthYear(1996)
                .setGender(Gender.FEMALE)
                .setPersonalPhone("+34676615106")
                .setEmail("raquelbarberosanchez90@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45134320V")
                .setCompleteName("Martín Lopes, Walter")
                .setBirthYear(1998)
                .setAge(26)
                .setAdult(true)
                .setGender("Male")
                .setPhones(List.of(
                    new PhoneDTO("+34722748406", PhoneType.PERSONAL.name())))
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45132337N")
                .setCompleteName("Barbero Sánchez, Raquel")
                .setBirthYear(1996)
                .setAge(28)
                .setAdult(true)
                .setGender("Female")
                .setPhones(List.of(
                    new PhoneDTO("+34676615106", PhoneType.PERSONAL.name())))
                .setEmail("raquelbarberosanchez90@gmail.com")
        ));
  }

  @Test
  @DisplayName("When list employees is empty return correctly list")
  void listEmployeesEmptyTest() {
    Mockito.when(employeeListHandler.listEmployees()).thenReturn(List.of());

    ResponseEntity<List<EmployeeResponseDTO>> result = controller.listEmployees();
    ResponseEntity<List<EmployeeResponseDTO>> expected = ResponseEntity.ok(List.of());

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(employeeListHandler, times(1)).listEmployees();
    Mockito.verify(mapper, never()).mapToResponseDTO(any(Employee.class));
  }

  @ParameterizedTest
  @MethodSource("getEmployeeByIdParameters")
  @DisplayName("Get employee by NIF returns employee and 200 response correctly")
  void getEmployeeByIdTest(String nif, Employee employee, EmployeeResponseDTO employeeResponseDTO) {
    EmployeeByIdQuery employeeByIdQuery = new EmployeeByIdQuery(nif);

    Mockito.when(mapper.mapToEmployeeByIdQuery(nif)).thenReturn(employeeByIdQuery);
    Mockito.when(employeeGetByIdHandler.getEmployeeById(employeeByIdQuery)).thenReturn(employee);
    Mockito.when(mapper.mapToResponseDTO(employee)).thenReturn(employeeResponseDTO);
    ResponseEntity<EmployeeResponseDTO> result = controller.getEmployeeById(nif);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(employeeResponseDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(mapper, times(1)).mapToEmployeeByIdQuery(nif);
    Mockito.verify(employeeGetByIdHandler, times(1)).getEmployeeById(employeeByIdQuery);
    Mockito.verify(mapper, times(1)).mapToResponseDTO(employee);
  }

  private static Stream<Arguments> getEmployeeByIdParameters() {
    return Stream.of(
        Arguments.of(
            "45134320V",
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45134320V")
                .setCompleteName("Martín Lopes, Walter")
                .setBirthYear(1998)
                .setAge(26)
                .setAdult(true)
                .setGender("Male")
                .setPhones(List.of(
                    new PhoneDTO("+34722748406", PhoneType.PERSONAL.name())))
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @Test
  @DisplayName("Get employee by NIF not found returns 404 response")
  void getEmployeeByIdNotFoundTest() {
    String nif = "45134320V";
    EmployeeByIdQuery employeeByIdQuery = new EmployeeByIdQuery(nif);

    Mockito.when(mapper.mapToEmployeeByIdQuery(nif)).thenReturn(employeeByIdQuery);
    Mockito.when(employeeGetByIdHandler.getEmployeeById(employeeByIdQuery)).thenReturn(null);
    ResponseEntity<EmployeeResponseDTO> result = controller.getEmployeeById(nif);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.notFound().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(mapper, times(1)).mapToEmployeeByIdQuery(nif);
    Mockito.verify(employeeGetByIdHandler, times(1)).getEmployeeById(employeeByIdQuery);
    Mockito.verify(mapper, never()).mapToResponseDTO(any(Employee.class));
  }

  @ParameterizedTest
  @MethodSource("getEmployeeByNameParameters")
  @DisplayName("Get employee by name returns employee and 200 response correctly")
  void getEmployeeByNameTest(String name, Employee employee, EmployeeResponseDTO employeeResponseDTO) {
    EmployeeByNameQuery employeeByNameQuery = new EmployeeByNameQuery(name);

    Mockito.when(mapper.mapToEmployeeByNameQuery(name)).thenReturn(employeeByNameQuery);
    Mockito.when(employeeGetByNameHandler.getEmployeeByName(employeeByNameQuery)).thenReturn(employee);
    Mockito.when(mapper.mapToResponseDTO(employee)).thenReturn(employeeResponseDTO);
    ResponseEntity<EmployeeResponseDTO> result = controller.getEmployeeByName(name);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(employeeResponseDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(mapper, times(1)).mapToEmployeeByNameQuery(name);
    Mockito.verify(employeeGetByNameHandler, times(1)).getEmployeeByName(employeeByNameQuery);
    Mockito.verify(mapper, times(1)).mapToResponseDTO(employee);
  }

  private static Stream<Arguments> getEmployeeByNameParameters() {
    return Stream.of(
        Arguments.of(
            "Walter",
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45134320V")
                .setCompleteName("Martín Lopes, Walter")
                .setBirthYear(1998)
                .setAge(26)
                .setAdult(true)
                .setGender("Male")
                .setPhones(List.of(
                    new PhoneDTO("+34722748406", PhoneType.PERSONAL.name())))
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Get employee by name not found returns 404 response")
  void getEmployeeByNameNotFoundTest(String name) {
    EmployeeByNameQuery employeeByNameQuery = new EmployeeByNameQuery(name);

    Mockito.when(mapper.mapToEmployeeByNameQuery(name)).thenReturn(employeeByNameQuery);
    Mockito.when(employeeGetByNameHandler.getEmployeeByName(employeeByNameQuery)).thenReturn(null);
    ResponseEntity<EmployeeResponseDTO> result = controller.getEmployeeByName(name);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.notFound().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(mapper, times(1)).mapToEmployeeByNameQuery(name);
    Mockito.verify(employeeGetByNameHandler, times(1)).getEmployeeByName(employeeByNameQuery);
    Mockito.verify(mapper, never()).mapToResponseDTO(any(Employee.class));
  }

  @ParameterizedTest
  @MethodSource("newEmployeeParameters")
  @DisplayName("Add new employee returns 201 response")
  void newEmployeeTest(EmployeeRequestDTO employeeRequest, EmployeeCreateCmd employeeCreateCmd, Employee employee,
      EmployeeResponseDTO employeeResponseDTO) {
    Mockito.when(mapper.mapToEmployeeCreateCmd(employeeRequest)).thenReturn(employeeCreateCmd);
    Mockito.when(employeeCreateHandler.addEmployee(employeeCreateCmd)).thenReturn(employee);
    Mockito.when(mapper.mapToResponseDTO(employee)).thenReturn(employeeResponseDTO);

    ResponseEntity<EmployeeResponseDTO> result = controller.newEmployee(employeeRequest);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.status(HttpStatus.CREATED).body(employeeResponseDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(employeeCreateHandler, times(1)).addEmployee(employeeCreateCmd);
    Mockito.verify(mapper, times(1)).mapToResponseDTO(any(Employee.class));
    Mockito.verify(mapper, times(1)).mapToEmployeeCreateCmd(any(EmployeeRequestDTO.class));
  }

  private static Stream<Arguments> newEmployeeParameters() {
    return Stream.of(
        Arguments.of(
            new EmployeeRequestDTO()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender("Male")
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeCreateCmd()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender("Male")
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com"),
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45134320V")
                .setCompleteName("Martín Lopes, Walter")
                .setBirthYear(1998)
                .setAge(26)
                .setAdult(true)
                .setGender("Male")
                .setPhones(List.of(
                    new PhoneDTO("+34722748406", PhoneType.PERSONAL.name())))
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @ParameterizedTest
  @MethodSource("updateEmployeeByIdParameters")
  @DisplayName("Update employee by NIF successfully returns 200 code response")
  void updateEmployeeByIdTest(String nif, EmployeeUpdateDTO employeeRequest, EmployeeUpdateCmd employeeUpdateCmd, Employee employee,
      EmployeeResponseDTO employeeResponseDTO) {

    Mockito.when(mapper.mapToEmployeeUpdateCmd(nif, employeeRequest)).thenReturn(employeeUpdateCmd);
    Mockito.when(employeeUpdateHandler.updateEmployee(employeeUpdateCmd)).thenReturn(employee);
    Mockito.when(mapper.mapToResponseDTO(employee)).thenReturn(employeeResponseDTO);

    ResponseEntity<EmployeeResponseDTO> result = controller.updateEmployeeById(nif, employeeRequest);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(employeeResponseDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(employeeUpdateHandler, times(1)).updateEmployee(employeeUpdateCmd);
    Mockito.verify(mapper, times(1)).mapToResponseDTO(any(Employee.class));
    Mockito.verify(mapper, times(1)).mapToEmployeeUpdateCmd(nif, employeeRequest);
  }

  private static Stream<Arguments> updateEmployeeByIdParameters() {
    return Stream.of(
        Arguments.of(
            "45134320V",
            new EmployeeUpdateDTO()
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender("Male")
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeUpdateCmd()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender("Male")
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com"),
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45134320V")
                .setCompleteName("Martín Lopes, Walter")
                .setBirthYear(1998)
                .setAge(26)
                .setAdult(true)
                .setGender("Male")
                .setPhones(List.of(
                    new PhoneDTO("+34722748406", PhoneType.PERSONAL.name())))
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @Test
  @DisplayName("Update employee by NIF not found returns 404 code response")
  void updateEmployeeByIdNotFoundTest() {
    String nif = "45134320V";
    EmployeeUpdateDTO employeeUpdateDTO = new EmployeeUpdateDTO()
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender("Male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");
    EmployeeUpdateCmd employeeUpdateCmd = new EmployeeUpdateCmd()
        .setNif(nif)
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender("Male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");

    Mockito.when(mapper.mapToEmployeeUpdateCmd(nif, employeeUpdateDTO)).thenReturn(employeeUpdateCmd);
    Mockito.when(employeeUpdateHandler.updateEmployee(employeeUpdateCmd)).thenReturn(null);

    Exception exception =
        Assertions.assertThrows(ResponseStatusException.class, () -> controller.updateEmployeeById(nif, employeeUpdateDTO));
    Assertions.assertTrue(exception.getMessage().contains("Employee not found."));
    Mockito.verify(mapper, times(1)).mapToEmployeeUpdateCmd(nif, employeeUpdateDTO);
    Mockito.verify(employeeUpdateHandler, times(1)).updateEmployee(employeeUpdateCmd);
    Mockito.verify(mapper, never()).mapToResponseDTO(any(Employee.class));
  }

  @ParameterizedTest
  @MethodSource("addEmployeeToCompanyParameters")
  @DisplayName("Add employee to company successfully returns 200 code response")
  void addEmployeeToCompanyTest(String nif, String cif, Company company, CompanyResponseDTO companyResponseDTO) {
    CompanyDTO companyDTO = new CompanyDTO(cif);
    AddEmployeeToCompanyCmd addEmployeeToCompanyCmd = new AddEmployeeToCompanyCmd(nif, cif);

    Mockito.when(mapper.mapToAddEmployeeToCompanyCmd(nif, companyDTO)).thenReturn(addEmployeeToCompanyCmd);
    Mockito.when(addEmployeeToCompanyHandler.addEmployeeToCompany(addEmployeeToCompanyCmd)).thenReturn(company);
    Mockito.when(companyMapper.mapToCompanyResponseDTO(company)).thenReturn(companyResponseDTO);

    ResponseEntity<CompanyResponseDTO> expected = ResponseEntity.ok(companyResponseDTO);
    ResponseEntity<CompanyResponseDTO> result = controller.addEmployeeToCompany(nif, companyDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    Mockito.verify(mapper, times(1)).mapToAddEmployeeToCompanyCmd(nif, companyDTO);
    Mockito.verify(addEmployeeToCompanyHandler, times(1)).addEmployeeToCompany(addEmployeeToCompanyCmd);
    Mockito.verify(companyMapper, times(1)).mapToCompanyResponseDTO(company);
  }

  private static Stream<Arguments> addEmployeeToCompanyParameters() {
    return Stream.of(
        Arguments.of(
            "45134320V",
            "Q4947066I",
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
                )),
            new CompanyResponseDTO()
                .setCif("Q4947066I")
                .setName("Company1 S.L")
                .setEmployees(List.of(
                    new EmployeeDTO()
                        .setNif("45134320V")
                        .setName("Walter")
                        .setSurname("Martín Lopes")
                        .setBirthYear(1998)
                        .setGender("MALE")
                        .setCompanyPhone("+34676615106")
                        .setPersonalPhone("+34722748406")
                        .setCompany("Q4947066I")
                        .setEmail("wmlopes0@gmail.com"),
                    new EmployeeDTO()
                        .setNif("45132337N")
                        .setName("Raquel")
                        .setSurname("Barbero Sánchez")
                        .setBirthYear(1996)
                        .setGender("FEMALE")
                        .setPersonalPhone("+34676615106")
                        .setCompany("Q4947066I")
                        .setEmail("raquelbarberosanchez90@gmail.com")
                ))
        )
    );
  }

  @Test
  @DisplayName("Add employee to company not found returns 404 code response")
  void addEmployeeToCompanyNotFoundTest() {
    String nif = "45134320V";
    String cif = "Q4947066I";
    CompanyDTO companyDTO = new CompanyDTO(cif);
    AddEmployeeToCompanyCmd addEmployeeToCompanyCmd = new AddEmployeeToCompanyCmd(nif, cif);

    Mockito.when(mapper.mapToAddEmployeeToCompanyCmd(nif, companyDTO)).thenReturn(addEmployeeToCompanyCmd);
    Mockito.when(addEmployeeToCompanyHandler.addEmployeeToCompany(addEmployeeToCompanyCmd)).thenReturn(null);

    ResponseEntity<CompanyResponseDTO> expected = ResponseEntity.notFound().build();
    ResponseEntity<CompanyResponseDTO> result = controller.addEmployeeToCompany(nif, companyDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    Mockito.verify(mapper, times(1)).mapToAddEmployeeToCompanyCmd(nif, companyDTO);
    Mockito.verify(addEmployeeToCompanyHandler, times(1)).addEmployeeToCompany(addEmployeeToCompanyCmd);
    Mockito.verify(companyMapper, never()).mapToCompanyResponseDTO(any(Company.class));
  }

  @Test
  @DisplayName("Remove employee from company successfully returns 200 code response")
  void removeEmployeeFromCompanyTest() {
    String nif = "45134320V";
    String cif = "Q4947066I";
    CompanyDTO companyDTO = new CompanyDTO(cif);
    RemoveEmployeeFromCompanyCmd removeEmployeeFromCompanyCmd = new RemoveEmployeeFromCompanyCmd(nif, cif);

    Mockito.when(mapper.removeEmployeeFromCompanyCmd(nif, companyDTO)).thenReturn(removeEmployeeFromCompanyCmd);
    Mockito.when(removeEmployeeFromCompanyHandler.removeEmployeeFromCompany(removeEmployeeFromCompanyCmd)).thenReturn(true);

    ResponseEntity<Object> expected = ResponseEntity.ok().build();
    ResponseEntity<Object> result = controller.removeEmployeeFromCompany(nif, companyDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    Mockito.verify(mapper, times(1)).removeEmployeeFromCompanyCmd(nif, companyDTO);
    Mockito.verify(removeEmployeeFromCompanyHandler, times(1)).removeEmployeeFromCompany(removeEmployeeFromCompanyCmd);
  }

  @Test
  @DisplayName("Remove employee from company not found returns 404 code response")
  void removeEmployeeFromCompanyNotFoundTest() {
    String nif = "45134320V";
    String cif = "Q4947066I";
    CompanyDTO companyDTO = new CompanyDTO(cif);
    RemoveEmployeeFromCompanyCmd removeEmployeeFromCompanyCmd = new RemoveEmployeeFromCompanyCmd(nif, cif);

    Mockito.when(mapper.removeEmployeeFromCompanyCmd(nif, companyDTO)).thenReturn(removeEmployeeFromCompanyCmd);
    Mockito.when(removeEmployeeFromCompanyHandler.removeEmployeeFromCompany(removeEmployeeFromCompanyCmd)).thenReturn(false);

    ResponseEntity<Object> expected = ResponseEntity.notFound().build();
    ResponseEntity<Object> result = controller.removeEmployeeFromCompany(nif, companyDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    Mockito.verify(mapper, times(1)).removeEmployeeFromCompanyCmd(nif, companyDTO);
    Mockito.verify(removeEmployeeFromCompanyHandler, times(1)).removeEmployeeFromCompany(removeEmployeeFromCompanyCmd);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "45134320V",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Deleted employee by NIF successfully returns 200 code response")
  void deleteEmployeeByIdTest(String nif) {
    EmployeeDeleteCmd employeeDeleteCmd = new EmployeeDeleteCmd(nif);

    Mockito.when(mapper.mapToEmployeeDeleteCmd(nif)).thenReturn(employeeDeleteCmd);
    Mockito.when(employeeDeleteHandler.deleteEmployee(employeeDeleteCmd)).thenReturn(true);
    ResponseEntity<Object> result = controller.deleteEmployeeById(nif);
    ResponseEntity<Object> expected = ResponseEntity.ok().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Mockito.verify(mapper, times(1)).mapToEmployeeDeleteCmd(nif);
    Mockito.verify(employeeDeleteHandler, times(1)).deleteEmployee(employeeDeleteCmd);
  }

  @Test
  @DisplayName("Delete employee by ID failed returns 404 code response.")
  void deleteEmployeeByIdNotFoundTest() {
    String nif = "45134320V";
    EmployeeDeleteCmd employeeDeleteCmd = new EmployeeDeleteCmd(nif);

    Mockito.when(mapper.mapToEmployeeDeleteCmd(nif)).thenReturn(employeeDeleteCmd);
    Mockito.when(employeeDeleteHandler.deleteEmployee(employeeDeleteCmd)).thenReturn(false);
    ResponseEntity<Object> result = controller.deleteEmployeeById(nif);
    ResponseEntity<Object> expected = ResponseEntity.notFound().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Mockito.verify(mapper, times(1)).mapToEmployeeDeleteCmd(nif);
    Mockito.verify(employeeDeleteHandler, times(1)).deleteEmployee(employeeDeleteCmd);
  }
}