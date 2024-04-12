package com.example.contract.employee.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import com.example.boot.app.App;
import com.example.domain.entity.Gender;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.entity.Phone;
import com.example.infrastructure.entity.PhoneType;
import com.example.infrastructure.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = App.class, properties = {"spring.profiles.active = test"})
@AutoConfigureMockMvc
class EmployeeRestControllerE2ETestIT {

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
    repository.save(new EmployeeEntity()
        .setNif("45134320V")
        .setName("Walter")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(List.of(new Phone("+34", "722748406", PhoneType.PERSONAL)))
        .setEmail("wmlopes0@gmail.com")
    );
    repository.save(new EmployeeEntity()
        .setNif("45132337N")
        .setName("Raquel")
        .setLastName("Barbero Sánchez")
        .setBirthYear(1996)
        .setGender(Gender.FEMALE.getCode())
        .setPhones(List.of(new Phone("+34", "676615106", PhoneType.COMPANY)))
        .setEmail("raquelbarberosanchez90@gmail.com")
    );

    String expected =
        """
            [
              {
               "nif": "45134320V",
               "completeName": "Martín Lopes, Walter",
               "birthYear": 1998,
               "age": 26,
               "adult": true,
               "gender": "Male",
               "phones": [
                   {
                       "number": "+34722748406",
                       "type": "PERSONAL"
                   }
               ],
               "email": "wmlopes0@gmail.com"
              },
            {
               "nif": "45132337N",
               "completeName": "Barbero Sánchez, Raquel",
               "birthYear": 1996,
               "age": 28,
               "adult": true,
               "gender": "Female",
               "phones": [
                   {
                       "number": "+34676615106",
                       "type": "COMPANY"
                   }
               ],
               "email": "raquelbarberosanchez90@gmail.com"
              },
            ]""";

    mockMvc.perform(get("/employees/"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, false));
  }

  @Test
  @DisplayName("When list employees is empty return correctly list")
  void listEmployeesEmptyTest() throws Exception {
    String expected = """
        []""";

    mockMvc.perform(get("/employees/"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, true));
  }

  @Test
  @DisplayName("Get employee by ID returns employee and 200 response correctly")
  void getEmployeeByIdTest() throws Exception {
    String id = "45134320V";
    repository.save(new EmployeeEntity()
        .setNif(id)
        .setName("Walter")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(List.of(new Phone("+34", "722748406", PhoneType.PERSONAL)))
        .setEmail("wmlopes0@gmail.com")
    );

    String expected = String.format("""
        {
               "nif": %s,
               "completeName": "Martín Lopes, Walter",
               "birthYear": 1998,
               "age": 26,
               "adult": true,
               "gender": "Male",
               "phones": [
                   {
                       "number": "+34722748406",
                       "type": "PERSONAL"
                   }
               ],
               "email": "wmlopes0@gmail.com"
              }""", id);

    mockMvc.perform(get("/employees/" + id))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, false));
  }

  @Test
  @DisplayName("Get employee by ID not found returns 404 response")
  void getEmployeeByIdNotFoundTest() throws Exception {
    String expected = "";

    mockMvc.perform(get("/employees/" + 1))
        .andExpect(status().isNotFound())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("Response for GetEmployeeById with null ID should be Internal Server Error")
  void getEmployeeByIdErrorTest() throws Exception {
    String expected = "";

    mockMvc.perform(get("/employees/" + null))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("Get employee by name returns employee and 200 response correctly")
  void getEmployeeByNameTest() throws Exception {
    repository.save(new EmployeeEntity()
        .setNif("45134320V")
        .setName("Walter")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(List.of(new Phone("+34", "722748406", PhoneType.PERSONAL)))
        .setEmail("wmlopes0@gmail.com")
    );

    String name = "Wal";
    String expected = """
        {
               "nif": "45134320V",
               "completeName": "Martín Lopes, Walter",
               "birthYear": 1998,
               "age": 26,
               "adult": true,
               "gender": "Male",
               "phones": [
                   {
                       "number": "+34722748406",
                       "type": "PERSONAL"
                   }
               ],
               "email": "wmlopes0@gmail.com"
              }""";

    mockMvc.perform(get("/employees/name/" + name))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, true));
  }

  @Test
  @DisplayName("Get employee by name not found returns 404 response")
  void getEmployeeByNameNotFoundTest() throws Exception {
    String name = "Wal";
    String expected = "";

    mockMvc.perform(get("/employees/name/" + name))
        .andExpect(status().isNotFound())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("Add new employee returns 201 response")
  void newEmployeeTest() throws Exception {
    String id = "45134320V";
    String jsonContent = String.format("""
        {
               "nif": %s,
               "name": "Walter",
               "surname": "Martín Lopes",
               "birthYear": 1998,
               "gender": "Male",
               "personalPhone": "+34722748406",
               "email": "wmlopes0@gmail.com"
              }""", id);

    String expected = String.format("""
        {
               "nif": %s,
               "completeName": "Martín Lopes, Walter",
               "birthYear": 1998,
               "age":26,
               "adult":true,
               "gender": "Male",
               "phones": [
                   {
                       "number": "+34722748406",
                       "type": "PERSONAL"
                   }
               ],
               "email": "wmlopes0@gmail.com"
              }""", id);

    EmployeeEntity employeeFetchExpected = new EmployeeEntity()
        .setNif(id)
        .setName("Walter")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(List.of(new Phone("+34", "722748406", PhoneType.PERSONAL)))
        .setEmail("wmlopes0@gmail.com");

    String result = mockMvc.perform(post("/employees/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Optional<EmployeeEntity> fetchExpected = Optional.of(employeeFetchExpected);
    Optional<EmployeeEntity> fetchResult = repository.findById(id);

    Assertions.assertEquals(expected, result);
    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @Test
  @DisplayName("Update employee by ID successfully returns 200 code response")
  void updateEmployeeByIdTest() throws Exception {
    String id = "45134320V";
    String updatedEmail = "walterlopesdiez@gmail.com";

    repository.save(new EmployeeEntity()
        .setNif(id)
        .setName("Walter")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(List.of(new Phone("+34", "722748406", PhoneType.PERSONAL)))
        .setEmail("wmlopes0@gmail.com")
    );

    String jsonContent = String.format("""
        {
               "nif": %s,
               "name": "Walter",
               "birthYear": 1998,
               "gender": "Male",
               "personalPhone": "+34722748406",
               "email": %s
              }""", id, updatedEmail);

    String expected = String.format("""
        {
               "nif": %s,
               "completeName": "Martín Lopes, Walter",
               "birthYear": 1998,
               "age":26,
               "adult":true,
               "gender": "Male",
               "phones": [
                   {
                       "number": "+34722748406",
                       "type": "PERSONAL"
                   }
               ],
               "email": %s
              }""", id, updatedEmail);

    EmployeeEntity employeeFetchExpected = new EmployeeEntity()
        .setNif(id)
        .setName("Walter")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(List.of(new Phone("+34", "722748406", PhoneType.PERSONAL)))
        .setEmail(updatedEmail);

    mockMvc.perform(put("/employees/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, true));

    Optional<EmployeeEntity> fetchExpected = Optional.of(employeeFetchExpected);
    Optional<EmployeeEntity> fetchResult = repository.findById(id);

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @Test
  @DisplayName("Update employee by ID not found returns 404 code response")
  void updateEmployeeByIdNotFoundTest() throws Exception {
    String id = "45134320V";
    String updatedEmail = "walterlopesdiez@gmail.com";
    String jsonContent = String.format("""
        {
               "nif": %s,
               "name": "Walter",
               "birthYear": 1998,
               "gender": "Male",
               "personalPhone": "+34722748406",
               "email": %s
              }""", id, updatedEmail);

    String expected = "";

    mockMvc.perform(put("/employees/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isNotFound())
        .andExpect(content().string(expected));
  }

  @Test
  @DisplayName("Deleted employee by ID successfully returns 200 code response")
  void deleteEmployeeByIdTest() throws Exception {
    String id = "45134320V";

    repository.save(new EmployeeEntity()
        .setNif(id)
        .setName("Walter")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(List.of(new Phone("+34", "722748406", PhoneType.PERSONAL)))
        .setEmail("wmlopes0@gmail.com")
    );

    mockMvc.perform(delete("/employees/{id}", id))
        .andExpect(status().isOk());

    Optional<EmployeeEntity> fetchExpected = Optional.empty();
    Optional<EmployeeEntity> fetchResult = repository.findById(id);

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @Test
  @DisplayName("Delete employee by ID failed returns 404 code response.")
  void deleteEmployeeByIdNotFoundTest() throws Exception {
    String id = "45134320V";
    mockMvc.perform(delete("/employees/{id}", id))
        .andExpect(status().isNotFound());
  }
}
