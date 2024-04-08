package com.example.contract.employee.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.example.boot.app.App;
import com.example.contract.employee.dto.EmployeeNameDTO;
import com.example.contract.employee.dto.EmployeeNameDetailsDTO;
import com.example.contract.employee.dto.EmployeeResponseDTO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = App.class, properties = {"spring.profiles.active = test"})
@AutoConfigureMockMvc
class EmployeeRestControllerTestIT {

  @Autowired
  private EmployeeRestController controller;

  @Autowired
  private EmployeeRepository repository;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void init() {
    repository.deleteAll();
  }

  @Test
  @DisplayName("List employees return correctly list")
  void listEmployeesTest() {
    Long id1 = repository.save(new EmployeeEntity().setName("Walter")).getNumber();
    Long id2 = repository.save(new EmployeeEntity().setName("Quique")).getNumber();
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
    Long id = repository.save(new EmployeeEntity().setName("Walter")).getNumber();

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
    Long id = repository.save(new EmployeeEntity().setName("Walter")).getNumber();

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

    //    ResponseEntity<EmployeeNameDetailsDTO> fetchExpected =
    //        ResponseEntity.ok(new EmployeeNameDetailsDTO().setNumber(result.getBody().getNumber()));
    //    ResponseEntity<EmployeeNameDetailsDTO> fetchResult = controller.getEmployeeById(result.getBody().getNumber());
    //
    //    Assertions.assertEquals(fetchExpected.getStatusCode(), fetchResult.getStatusCode());
    //    Assertions.assertEquals(fetchExpected.getBody(), fetchResult.getBody());
  }

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Update employee by ID successfully returns 200 code response")
  void updateEmployeeByIdTest(String name) {
    Long id = repository.save(new EmployeeEntity().setName(name)).getNumber();
    String newName = "Pepito";

    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(new EmployeeResponseDTO(id, newName));
    ResponseEntity<EmployeeResponseDTO> result = controller.updateEmployeeById(id, new EmployeeNameDTO(newName));

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());

    ResponseEntity<EmployeeNameDetailsDTO> fetchExpected =
        ResponseEntity.ok(new EmployeeNameDetailsDTO(id, newName.toUpperCase(), newName.length()));
    ResponseEntity<EmployeeNameDetailsDTO> fetchResult = controller.getEmployeeById(id);

    Assertions.assertEquals(fetchExpected.getStatusCode(), fetchResult.getStatusCode());
    Assertions.assertEquals(fetchExpected.getBody(), fetchResult.getBody());
  }

  @Test
  @DisplayName("Update employee by ID not found returns 404 code response")
  void updateEmployeeByIdNotFoundTest() throws Exception {
    Long id = 1L;
    String newName = "Pepito";

    mockMvc.perform(put("/employees/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                "name": "Walter"
                }
                """))
        //            .content(objectMapper.writeValueAsString(new EmployeeNameDTO(newName))))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Deleted employee by ID successfully returns 200 code response")
  void deleteEmployeeByIdTest() {
    Long id = repository.save(new EmployeeEntity().setName("Walter")).getNumber();

    ResponseEntity<Object> deleteExpected = ResponseEntity.ok().build();
    ResponseEntity<Object> deleteResult = controller.deleteEmployeeById(id);

    Assertions.assertEquals(deleteExpected.getStatusCode(), deleteResult.getStatusCode());

    ResponseEntity<EmployeeNameDetailsDTO> fetchExpected = ResponseEntity.notFound().build();
    ResponseEntity<EmployeeNameDetailsDTO> fetchResult = controller.getEmployeeById(id);

    Assertions.assertEquals(fetchExpected.getStatusCode(), fetchResult.getStatusCode());
    Assertions.assertEquals(fetchExpected.getBody(), fetchResult.getBody());
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

