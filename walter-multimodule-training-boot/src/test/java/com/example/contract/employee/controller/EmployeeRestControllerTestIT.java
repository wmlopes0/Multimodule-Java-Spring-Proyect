package com.example.contract.employee.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.example.boot.app.App;
import com.example.contract.employee.dto.EmployeeNifDTO;
import com.example.contract.employee.dto.EmployeeRequestDTO;
import com.example.contract.employee.dto.EmployeeResponseDTO;
import com.example.contract.employee.dto.EmployeeUpdateDTO;
import com.example.contract.employee.dto.PhoneDTO;
import com.example.domain.entity.Gender;
import com.example.domain.entity.Phone;
import com.example.domain.entity.PhoneType;
import com.example.infrastructure.entity.EmployeeEntity;
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
class EmployeeRestControllerTestIT {

  @Autowired
  private EmployeeRestController controller;

  @Autowired
  private EmployeeRepository repository;

  @BeforeEach
  void init() {
    repository.deleteAll();
  }

  @ParameterizedTest
  @MethodSource("listEmployeesParameters")
  @DisplayName("List employees return correctly list")
  void listEmployeesTest(EmployeeEntity employee1, EmployeeEntity employee2, List<EmployeeResponseDTO> employeeResponseDTOS) {
    repository.save(employee1);
    repository.save(employee2);
    ResponseEntity<List<EmployeeResponseDTO>> expected = ResponseEntity.ok(employeeResponseDTOS);

    ResponseEntity<List<EmployeeResponseDTO>> result = controller.listEmployees();
    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  private static Stream<Arguments> listEmployeesParameters() {
    return Stream.of(
        Arguments.of(
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new Phone("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeEntity()
                .setNif("45132337N")
                .setName("Raquel")
                .setLastName("Barbero Sánchez")
                .setBirthYear(1996)
                .setGender(Gender.FEMALE.getCode())
                .setPhones(List.of(new Phone("+34", "676615106", PhoneType.PERSONAL)))
                .setEmail("raquelbarberosanchez90@gmail.com"),
            List.of(
                new EmployeeResponseDTO()
                    .setNif("45134320V")
                    .setCompleteName("Martín Lopes, Walter")
                    .setBirthYear(1998)
                    .setAge(26)
                    .setAdult(true)
                    .setGender("Male")
                    .setPhones(List.of(
                        new PhoneDTO("+34722748406", PhoneType.PERSONAL.name()),
                        new PhoneDTO(null, PhoneType.COMPANY.name())))
                    .setEmail("wmlopes0@gmail.com"),
                new EmployeeResponseDTO()
                    .setNif("45132337N")
                    .setCompleteName("Barbero Sánchez, Raquel")
                    .setBirthYear(1996)
                    .setAge(28)
                    .setAdult(true)
                    .setGender("Female")
                    .setPhones(List.of(
                        new PhoneDTO("+34676615106", PhoneType.PERSONAL.name()),
                        new PhoneDTO(null, PhoneType.COMPANY.name())))
                    .setEmail("raquelbarberosanchez90@gmail.com")
            )
        )
    );
  }

  @Test
  @DisplayName("When list employees is empty return correctly list")
  void listEmployeesEmptyTest() {
    ResponseEntity<List<EmployeeResponseDTO>> expected = ResponseEntity.ok(List.of());
    ResponseEntity<List<EmployeeResponseDTO>> result = controller.listEmployees();
    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @ParameterizedTest
  @MethodSource("getEmployeeByIdParameters")
  @DisplayName("Get employee by NIF returns employee and 200 response correctly")
  void getEmployeeByIdTest(EmployeeNifDTO employeeNifDTO, EmployeeEntity employeeEntity, EmployeeResponseDTO employeeResponseDTO) {
    repository.save(employeeEntity);

    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(employeeResponseDTO);
    ResponseEntity<EmployeeResponseDTO> result = controller.getEmployeeById(employeeNifDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  private static Stream<Arguments> getEmployeeByIdParameters() {
    return Stream.of(
        Arguments.of(
            new EmployeeNifDTO("45134320V"),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new Phone("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45134320V")
                .setCompleteName("Martín Lopes, Walter")
                .setBirthYear(1998)
                .setAge(26)
                .setAdult(true)
                .setGender("Male")
                .setPhones(List.of(
                    new PhoneDTO("+34722748406", PhoneType.PERSONAL.name()),
                    new PhoneDTO(null, PhoneType.COMPANY.name())))
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @Test
  @DisplayName("Get employee by NIF not found returns 404 response")
  void getEmployeeByIdNotFoundTest() {
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.notFound().build();
    ResponseEntity<EmployeeResponseDTO> result = controller.getEmployeeById(new EmployeeNifDTO("45134320V"));

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @ParameterizedTest
  @MethodSource("getEmployeeByNameParameters")
  @DisplayName("Get employee by name returns employee and 200 response correctly")
  void getEmployeeByNameTest(String name, EmployeeEntity employeeEntity, EmployeeResponseDTO employeeResponseDTO) {
    repository.save(employeeEntity);

    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(employeeResponseDTO);
    ResponseEntity<EmployeeResponseDTO> result = controller.getEmployeeByName(name);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  private static Stream<Arguments> getEmployeeByNameParameters() {
    return Stream.of(
        Arguments.of(
            "Wal",
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new Phone("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45134320V")
                .setCompleteName("Martín Lopes, Walter")
                .setBirthYear(1998)
                .setAge(26)
                .setAdult(true)
                .setGender("Male")
                .setPhones(List.of(
                    new PhoneDTO("+34722748406", PhoneType.PERSONAL.name()),
                    new PhoneDTO(null, PhoneType.COMPANY.name())))
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @Test
  @DisplayName("Get employee by name not found returns 404 response")
  void getEmployeeByNameNotFoundTest() {
    String name = "Wal";

    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.notFound().build();
    ResponseEntity<EmployeeResponseDTO> result = controller.getEmployeeByName(name);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @ParameterizedTest
  @MethodSource("newEmployeeParameters")
  @DisplayName("Add new employee returns 201 response")
  void newEmployeeTest(EmployeeRequestDTO employeeRequestDTO, EmployeeResponseDTO employeeResponseDTO, EmployeeEntity entityExpected) {
    ResponseEntity<EmployeeResponseDTO> result = controller.newEmployee(employeeRequestDTO);
    ResponseEntity<EmployeeResponseDTO> expected =
        ResponseEntity.status(HttpStatus.CREATED).body(employeeResponseDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    Optional<EmployeeEntity> fetchExpected = Optional.of(entityExpected);
    Optional<EmployeeEntity> fetchResult = repository.findById(employeeRequestDTO.getNif());

    Assertions.assertEquals(fetchExpected, fetchResult);
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
            new EmployeeResponseDTO()
                .setNif("45134320V")
                .setCompleteName("Martín Lopes, Walter")
                .setBirthYear(1998)
                .setAge(26)
                .setAdult(true)
                .setGender("Male")
                .setPhones(List.of(
                    new PhoneDTO("+34722748406", PhoneType.PERSONAL.name()),
                    new PhoneDTO(null, PhoneType.COMPANY.name())))
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new Phone("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @ParameterizedTest
  @MethodSource("updateEmployeeByIdParameters")
  @DisplayName("Update employee by NIF successfully returns 200 code response")
  void updateEmployeeByIdTest(EmployeeEntity employeeEntity, EmployeeUpdateDTO employeeUpdateDTO,
      EmployeeResponseDTO employeeResponseDTO, EmployeeEntity entityExpected) {
    repository.save(employeeEntity);

    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(employeeResponseDTO);
    ResponseEntity<EmployeeResponseDTO> result = controller.updateEmployeeById(employeeUpdateDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    Optional<EmployeeEntity> fetchExpected = Optional.of(entityExpected);
    Optional<EmployeeEntity> fetchResult = repository.findById(employeeUpdateDTO.getNif());

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  private static Stream<Arguments> updateEmployeeByIdParameters() {
    return Stream.of(
        Arguments.of(
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(
                    new Phone("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeUpdateDTO()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1999)
                .setGender("Male")
                .setPersonalPhone("+34722748406")
                .setCompanyPhone("+34676615106")
                .setEmail("walterlopesdiez@gmail.com"),
            new EmployeeResponseDTO()
                .setNif("45134320V")
                .setCompleteName("Martín Lopes, Walter")
                .setBirthYear(1999)
                .setAge(25)
                .setAdult(true)
                .setGender("Male")
                .setPhones(List.of(
                    new PhoneDTO("+34722748406", PhoneType.PERSONAL.name()),
                    new PhoneDTO("+34676615106", PhoneType.COMPANY.name())))
                .setEmail("walterlopesdiez@gmail.com"),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1999)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(
                    new Phone("+34", "676615106", PhoneType.COMPANY),
                    new Phone("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("walterlopesdiez@gmail.com")
        )
    );
  }

  @Test
  @DisplayName("Deleted employee by NIF successfully returns 200 code response")
  void deleteEmployeeByIdTest() {
    EmployeeNifDTO employeeNifDTO = new EmployeeNifDTO("45134320V");
    EmployeeEntity employeeEntity = new EmployeeEntity()
        .setNif("45134320V")
        .setName("Walter")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(List.of(
            new Phone("+34", "722748406", PhoneType.PERSONAL)))
        .setEmail("wmlopes0@gmail.com");

    repository.save(employeeEntity);

    ResponseEntity<Object> deleteExpected = ResponseEntity.ok().build();
    ResponseEntity<Object> deleteResult = controller.deleteEmployeeById(employeeNifDTO);

    Assertions.assertEquals(deleteExpected.getStatusCode(), deleteResult.getStatusCode());

    Optional<EmployeeEntity> fetchResult = repository.findById(employeeEntity.getNif());
    Assertions.assertTrue(fetchResult.isEmpty());
  }

  @Test
  @DisplayName("Delete employee by NIF failed returns 404 code response.")
  void deleteEmployeeByIdNotFoundTest() {
    ResponseEntity<Object> expected = ResponseEntity.notFound().build();
    ResponseEntity<Object> result = controller.deleteEmployeeById(new EmployeeNifDTO("45134320V"));

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }
}

