package com.example.contract.employee.controller;

import java.util.List;
import java.util.Optional;

import com.example.boot.app.App;
import com.example.contract.employee.dto.EmployeeNameDTO;
import com.example.contract.employee.dto.EmployeeResponseDTO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

  @Test
  @DisplayName("List employees return correctly list")
  void listEmployeesTest() {
    Long id1 = repository.save(new EmployeeEntity().setNumber(1L).setName("Walter")).getNumber();
    Long id2 = repository.save(new EmployeeEntity().setNumber(2L).setName("Quique")).getNumber();
    ResponseEntity<List<EmployeeNameDetailsDTO>> expected = ResponseEntity.ok(
        List.of(new EmployeeNameDetailsDTO(id1, "WALTER", 6),
            new EmployeeNameDetailsDTO(id2, "QUIQUE", 6)));

    ResponseEntity<List<EmployeeNameDetailsDTO>> result = controller.listEmployees();
    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @Test
  @DisplayName("When list employees is empty return correctly list")
  void listEmployeesEmptyTest() {
    ResponseEntity<List<EmployeeNameDetailsDTO>> expected = ResponseEntity.ok(List.of());
    ResponseEntity<List<EmployeeNameDetailsDTO>> result = controller.listEmployees();
    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @Test
  @DisplayName("Get employee by ID returns employee and 200 response correctly")
  void getEmployeeByIdTest() {
    Long id = repository.save(new EmployeeEntity().setNumber(1L).setName("Walter")).getNumber();

    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.ok(new EmployeeNameDetailsDTO(id, "WALTER", 6));
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeById(id);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @Test
  @DisplayName("Get employee by ID not found returns 404 response")
  void getEmployeeByIdNotFoundTest() {
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.notFound().build();
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeById(1L);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @Test
  @DisplayName("Response for GetEmployeeById with null ID should be Internal Server Error")
  void getEmployeeByIdErrorTest() {
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.internalServerError().build();
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeById(null);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @Test
  @DisplayName("Get employee by name returns employee and 200 response correctly")
  void getEmployeeByNameTest() {
    String name = "Wal";
    Long id = repository.save(new EmployeeEntity().setNumber(1L).setName("Walter")).getNumber();

    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.ok(new EmployeeNameDetailsDTO(id, "WALTER", 6));
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeByName(name);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @Test
  @DisplayName("Get employee by name not found returns 404 response")
  void getEmployeeByNameNotFoundTest() {
    String name = "Wal";

    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.notFound().build();
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeByName(name);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Add new employee returns 201 response")
  void newEmployeeTest(String name) {
    EmployeeNameDTO requestBody = new EmployeeNameDTO(name);

    ResponseEntity<EmployeeResponseDTO> result = controller.newEmployee(requestBody);
    ResponseEntity<EmployeeResponseDTO> expected =
        ResponseEntity.status(HttpStatus.CREATED).body(new EmployeeResponseDTO(result.getBody().getNumber(), name));

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    Optional<EmployeeEntity> fetchExpected = Optional.of(new EmployeeEntity(result.getBody().getNumber(), name));
    Optional<EmployeeEntity> fetchResult = repository.findById(result.getBody().getNumber());

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Update employee by ID successfully returns 200 code response")
  void updateEmployeeByIdTest(String name) {
    Long id = repository.save(new EmployeeEntity().setNumber(1L).setName(name)).getNumber();
    String newName = "Pepito";

    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(new EmployeeResponseDTO(id, newName));
    ResponseEntity<EmployeeResponseDTO> result = controller.updateEmployeeById(id, new EmployeeNameDTO(newName));

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    Optional<EmployeeEntity> fetchExpected = Optional.of(new EmployeeEntity(id, newName));
    Optional<EmployeeEntity> fetchResult = repository.findById(id);

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @Test
  @DisplayName("Deleted employee by ID successfully returns 200 code response")
  void deleteEmployeeByIdTest() {
    Long id = repository.save(new EmployeeEntity().setNumber(1L).setName("Walter")).getNumber();

    ResponseEntity<Object> deleteExpected = ResponseEntity.ok().build();
    ResponseEntity<Object> deleteResult = controller.deleteEmployeeById(id);

    Assertions.assertEquals(deleteExpected.getStatusCode(), deleteResult.getStatusCode());

    Optional<EmployeeEntity> fetchResult = repository.findById(id);
    Assertions.assertTrue(fetchResult.isEmpty());
  }

  @Test
  @DisplayName("Delete employee by ID failed returns 404 code response.")
  void deleteEmployeeByIdNotFoundTest() {
    ResponseEntity<Object> expected = ResponseEntity.notFound().build();
    ResponseEntity<Object> result = controller.deleteEmployeeById(1L);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @Test
  @DisplayName("Response for DeleteEmployeeById with null ID should be Internal Server Error")
  void deleteEmployeeByIdErrorTest() {
    ResponseEntity<Object> expected = ResponseEntity.internalServerError().build();
    ResponseEntity<Object> result = controller.deleteEmployeeById(null);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }
}

