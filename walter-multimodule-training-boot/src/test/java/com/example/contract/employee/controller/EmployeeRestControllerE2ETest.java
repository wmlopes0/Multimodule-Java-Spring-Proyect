package com.example.contract.employee.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import com.example.boot.app.App;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.repository.EmployeeRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = App.class, properties = {"spring.profiles.active = test"})
@AutoConfigureMockMvc
class EmployeeRestControllerE2ETest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private EmployeeRepository repository;

  @BeforeEach
  void init() {
    repository.deleteAll();
  }

  @Test
  @DisplayName("List employees return correctly list")
  void listEmployeesTest() throws Exception {
    Long id1 = repository.save(new EmployeeEntity().setName("Walter")).getNumber();
    Long id2 = repository.save(new EmployeeEntity().setName("Quique")).getNumber();

    String expected =
        String.format("""
            [{"number":%d,"name":"WALTER","nameLength":6},{"number":%d,"name":"QUIQUE","nameLength":6}]""", id1, id2);

    String result = mockMvc.perform(get("/employees/"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("When list employees is empty return correctly list")
  void listEmployeesEmptyTest() throws Exception {
    String expected = """
        []""";

    String result = mockMvc.perform(get("/employees/"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Get employee by ID returns employee and 200 response correctly")
  void getEmployeeByIdTest() throws Exception {
    Long id1 = repository.save(new EmployeeEntity().setName("Walter")).getNumber();

    String expected =
        String.format("""
            {"number":%d,"name":"WALTER","nameLength":6}""", id1);

    String result = mockMvc.perform(get("/employees/" + id1))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();
    System.out.println(result);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Get employee by ID not found returns 404 response")
  void getEmployeeByIdNotFoundTest() throws Exception {
    String expected = "";

    String result = mockMvc.perform(get("/employees/" + 1))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Response for GetEmployeeById with null ID should be Internal Server Error")
  void getEmployeeByIdErrorTest() throws Exception {
    String expected = "";

    String result = mockMvc.perform(get("/employees/" + null))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Get employee by name returns employee and 200 response correctly")
  void getEmployeeByNameTest() throws Exception {
    Long id1 = repository.save(new EmployeeEntity().setName("Walter")).getNumber();
    String name = "Wal";
    String expected =
        String.format("""
            {"number":%d,"name":"WALTER","nameLength":6}""", id1);

    String result = mockMvc.perform(get("/employees/name/" + name))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Get employee by name not found returns 404 response")
  void getEmployeeByNameNotFoundTest() throws Exception {
    String name = "Wal";
    String expected = "";

    String result = mockMvc.perform(get("/employees/name/" + name))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Add new employee returns 201 response")
  @Transactional
  void newEmployeeTest() throws Exception {
    String name = "Walter";
    String jsonContent = String.format("""
        {"name":"%s"}""", name);

    String result = mockMvc.perform(post("/employees/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Integer number = JsonPath.read(result, "$.number");

    Optional<EmployeeEntity> fetchExpected = Optional.of(new EmployeeEntity(number.longValue(), name));
    Optional<EmployeeEntity> fetchResult = repository.findById(number.longValue());

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @Test
  @DisplayName("Add new employee with name null returns 201 response")
  @Transactional
  void newEmployeeNameNullTest() throws Exception {
    String name = null;
    String jsonContent = String.format("""
        {"name":%s}""", name);

    String result = mockMvc.perform(post("/employees/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Integer number = JsonPath.read(result, "$.number");

    Optional<EmployeeEntity> fetchExpected = Optional.of(new EmployeeEntity(number.longValue(), name));
    Optional<EmployeeEntity> fetchResult = repository.findById(number.longValue());

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @Test
  @DisplayName("Update employee by ID successfully returns 200 code response")
  @Transactional
  void updateEmployeeByIdTest() throws Exception {
    Long id = repository.save(new EmployeeEntity().setName("Walter")).getNumber();
    String newName = "ChangedName";
    String jsonContent = String.format("""
        {"name":"%s"}""", newName);

    String expected = String.format("""
        {"number":%d,"name":"%s"}""", id, newName);

    String result = mockMvc.perform(put("/employees/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Optional<EmployeeEntity> fetchExpected = Optional.of(new EmployeeEntity(id, newName));
    Optional<EmployeeEntity> fetchResult = repository.findById(id);

    Assertions.assertEquals(expected, result);
    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @Test
  @DisplayName("Update employee by ID with name null successfully returns 200 code response")
  @Transactional
  void updateEmployeeByIdNameNullTest() throws Exception {
    Long id = repository.save(new EmployeeEntity().setName("Walter")).getNumber();
    String newName = null;
    String jsonContent = String.format("""
        {"name":%s}""", newName);

    String expected = String.format("""
        {"number":%d,"name":%s}""", id, newName);

    String result = mockMvc.perform(put("/employees/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Optional<EmployeeEntity> fetchExpected = Optional.of(new EmployeeEntity(id, newName));
    Optional<EmployeeEntity> fetchResult = repository.findById(id);

    Assertions.assertEquals(expected, result);
    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @Test
  @DisplayName("Update employee by ID not found returns 404 code response")
  void updateEmployeeByIdNotFoundTest() throws Exception {
    Long id = 1L;
    String newName = "ChangedName";
    String jsonContent = String.format("""
        {"name":"%s"}""", newName);

    String expected = "";

    String result = mockMvc.perform(put("/employees/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Deleted employee by ID successfully returns 200 code response")
  @Transactional
  void deleteEmployeeByIdTest() throws Exception {
    Long id = repository.save(new EmployeeEntity().setName("Walter")).getNumber();

    mockMvc.perform(delete("/employees/{id}", id))
        .andExpect(status().isOk());

    Optional<EmployeeEntity> fetchExpected = Optional.empty();
    Optional<EmployeeEntity> fetchResult = repository.findById(id);

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @Test
  @DisplayName("Delete employee by ID failed returns 404 code response.")
  void deleteEmployeeByIdNotFoundTest() throws Exception {
    Long id = 1L;
    mockMvc.perform(delete("/employees/{id}", id))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Response for DeleteEmployeeById with null ID should be Internal Server Error")
  void deleteEmployeeByIdErrorTest() throws Exception {
    Long id = null;
    mockMvc.perform(delete("/employees/{id}", id))
        .andExpect(status().isMethodNotAllowed());
  }
}
