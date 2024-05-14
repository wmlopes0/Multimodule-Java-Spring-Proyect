package com.example.contract.employee.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.example.boot.app.App;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
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
  private EmployeeRepository employeeRepository;

  @Autowired
  private CompanyRepository companyRepository;

  @BeforeEach
  void init() {
    employeeRepository.deleteAll();
    companyRepository.deleteAll();
  }

  @ParameterizedTest
  @MethodSource("listEmployeesParameters")
  @DisplayName("List employees return correctly list")
  void listEmployeesTest(EmployeeEntity employeeEntity1, EmployeeEntity employeeEntity2, String expected) throws Exception {
    employeeRepository.save(employeeEntity1);
    employeeRepository.save(employeeEntity2);

    mockMvc.perform(get("/employees/"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, false));
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
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeEntity()
                .setNif("45132337N")
                .setName("Raquel")
                .setLastName("Barbero Sánchez")
                .setBirthYear(1996)
                .setGender(Gender.FEMALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "676615106", PhoneType.COMPANY)))
                .setEmail("raquelbarberosanchez90@gmail.com"),
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
                  }
                ]"""
        )
    );
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

  @ParameterizedTest
  @MethodSource("getEmployeeByIdParameters")
  @DisplayName("Get employee by ID returns employee and 200 response correctly")
  void getEmployeeByIdTest(String nif, EmployeeEntity employeeEntity, String expected) throws Exception {
    employeeRepository.save(employeeEntity);

    mockMvc.perform(get("/employees/{nif}", nif))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, false));
  }

  private static Stream<Arguments> getEmployeeByIdParameters() {
    return Stream.of(
        Arguments.of(
            "45134320V",
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            """
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
                    }
                """)
    );
  }

  @Test
  @DisplayName("Get employee by NIF not found returns 404 response")
  void getEmployeeByIdNotFoundTest() throws Exception {
    String expected = "";
    String nif = "45134320V";

    mockMvc.perform(get("/employees/{nif}", nif))
        .andExpect(status().isNotFound())
        .andExpect(content().string(expected));
  }

  @ParameterizedTest
  @CsvSource(value = {
      "45134320Z",
      "4513",
  }, nullValues = "null")
  @DisplayName("Invalid NIF should result in HTTP 500 InternalServerError")
  void getEmployeeByIdInvalidTest(String nif) throws Exception {
    String expected = """
        {
         "nif":"Invalid NIF"
        }
        """;

    mockMvc.perform(get("/employees/{nif}", nif))
        .andExpect(status().isBadRequest())
        .andExpect(content().json(expected, false));
  }

  @ParameterizedTest
  @MethodSource("getEmployeeByNameParameters")
  @DisplayName("Get employee by name returns employee and 200 response correctly")
  void getEmployeeByNameTest(String name, EmployeeEntity employeeEntity, String expected) throws Exception {
    employeeRepository.save(employeeEntity);

    mockMvc.perform(get("/employees/name/{name}", name))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, true));
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
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            """
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
                }""")
    );
  }

  @Test
  @DisplayName("Get employee by name not found returns 404 response")
  void getEmployeeByNameNotFoundTest() throws Exception {
    String name = "Wal";
    String expected = "";

    mockMvc.perform(get("/employees/name/{name}", name))
        .andExpect(status().isNotFound())
        .andExpect(content().string(expected));
  }

  @ParameterizedTest
  @MethodSource("newEmployeeParameters")
  @DisplayName("Add new employee returns 201 response")
  void newEmployeeTest(String nif, String jsonContent, String expected, EmployeeEntity entityExpected) throws Exception {
    mockMvc.perform(post("/employees/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isCreated())
        .andExpect(content().json(expected, false));

    Optional<EmployeeEntity> fetchExpected = Optional.of(entityExpected);
    Optional<EmployeeEntity> fetchResult = employeeRepository.findById(nif);

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  private static Stream<Arguments> newEmployeeParameters() {
    return Stream.of(
        Arguments.of(
            "45134320V",
            """
                {
                       "nif": "45134320V",
                       "name": "Walter",
                       "surname": "Martín Lopes",
                       "birthYear": 1998,
                       "gender": "MALE",
                       "personalPhone": "+34722748406",
                       "email": "wmlopes0@gmail.com"
                      }""",
            """
                {
                       "nif": "45134320V",
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
                      }""",
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @ParameterizedTest
  @MethodSource("newEmployeeInvalidParameters")
  @DisplayName("Creating an employee with invalid parameters should return HTTP 400 BadRequest")
  void newEmployeeInvalidParametersTest(String jsonContent, String expected) throws Exception {
    mockMvc.perform(post("/employees/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isBadRequest())
        .andExpect(content().json(expected, false));
  }

  private static Stream<Arguments> newEmployeeInvalidParameters() {
    return Stream.of(
        Arguments.of(
            """
                {
                       "nif": "451343260V",
                       "name": "",
                       "birthYear": 2025,
                       "gender": "Binary",
                       "personalPhone": "722748406",
                       "email": "wmlopes0.gmail.com"
                      }""",
            """
                {
                       "nif": "Invalid NIF",
                       "name": "Name cannot be empty",
                       "gender": "Gender must be either 'MALE' or 'FEMALE'",
                       "personalPhone": "Invalid phone format",
                       "email": "Invalid email format"
                      }"""

        ),
        Arguments.of(
            """
                {
                       "surname": "Martín Lopes",
                       "companyPhone": "+34722748406",
                       "email": "wmlopes0@gmail.com"
                      }""",
            """
                {
                       "nif": "NIF cannot be null",
                       "name": "Name cannot be null",
                       "gender": "Gender cannot be null",
                       "personalPhone": "Personal phone cannot be null"
                      }"""

        )
    );
  }

  @ParameterizedTest
  @MethodSource("updateEmployeeByIdParameters")
  @DisplayName("Update employee by ID successfully returns 200 code response")
  void updateEmployeeByIdTest(String nif, EmployeeEntity employeeEntity, String jsonContent, String expected, EmployeeEntity entityExpected)
      throws Exception {
    employeeRepository.save(employeeEntity);

    mockMvc.perform(put("/employees/{nif}", nif)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, false));

    Optional<EmployeeEntity> fetchExpected = Optional.of(entityExpected);
    Optional<EmployeeEntity> fetchResult = employeeRepository.findById(nif);

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  private static Stream<Arguments> updateEmployeeByIdParameters() {
    return Stream.of(
        Arguments.of(
            "45134320V",
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            """
                {
                       "name": "Walter",
                       "surname":"Martín Lopes",
                       "birthYear": 1998,
                       "gender": "MALE",
                       "personalPhone": "+34722748406",
                       "email": "walterlopesdiez@gmail.com"
                      }""",
            """
                {
                       "nif": "45134320V",
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
                       "email": "walterlopesdiez@gmail.com"
                      }""",
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("walterlopesdiez@gmail.com")

        )
    );
  }

  @ParameterizedTest
  @MethodSource("updateEmployeeInvalidParameters")
  @DisplayName("Updating an employee with invalid parameters should return HTTP 400 BadRequest")
  void updateEmployeeInvalidParametersTest(String nif, String jsonContent, String expected) throws Exception {
    mockMvc.perform(put("/employees/{nif}", nif)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(content().json(expected, false));
  }

  private static Stream<Arguments> updateEmployeeInvalidParameters() {
    return Stream.of(
        Arguments.of(
            "45134320V",
            """
                {
                       "name": "",
                       "birthYear": 2025,
                       "gender": "Binary",
                       "personalPhone": "722748406",
                       "email": "wmlopes0.gmail.com"
                      }""",
            """
                {
                       "name": "Name cannot be empty",
                       "gender": "Gender must be either 'MALE' or 'FEMALE'",
                       "personalPhone": "Invalid phone format",
                       "email": "Invalid email format"
                      }"""

        )
    );
  }

  @Test
  @DisplayName("Update employee by ID not found returns 404 code response")
  void updateEmployeeByIdNotFoundTest() throws Exception {
    String nif = "45134320V";
    String jsonContent = """
        {
               "name": "Walter",
               "surname": "Martín Lopes",
               "birthYear": 1998,
               "gender": "MALE",
               "personalPhone": "+34722748406",
               "email": "walterlopesdiez@gmail.com"
              }""";

    mockMvc.perform(put("/employees/{nif}", nif)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isNotFound())
        .andExpect(content().string(""));
  }

  @ParameterizedTest
  @MethodSource("addEmployeeToCompanyParameters")
  @DisplayName("Add employee to company successfully returns 200 code response")
  void addEmployeeToCompany(EmployeeEntity employeeEntity, CompanyEntity companyEntity, String expected,
      EmployeeEntity employeeEntityExpected,
      CompanyEntity companyEntityExpected) throws Exception {

    employeeRepository.save(employeeEntity);
    companyRepository.save(companyEntity);

    String nif = "45134320V";
    String jsonContent = """
        {
               "cif": "B86017472"
              }""";

    mockMvc.perform(put("/employees/addToCompany/{nif}", nif)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isOk())
        .andExpect(content().json(expected, false));

    Optional<EmployeeEntity> fetchEmployeeExpected = Optional.of(employeeEntityExpected);
    Optional<EmployeeEntity> fetchEmployeeResult = employeeRepository.findById(nif);

    Assertions.assertEquals(fetchEmployeeExpected, fetchEmployeeResult);

    Optional<CompanyEntity> fetchCompanyExpected = Optional.of(companyEntityExpected);
    Optional<CompanyEntity> fetchCompanyResult = companyRepository.findById("B86017472");

    Assertions.assertEquals(fetchCompanyExpected, fetchCompanyResult);
  }

  private static Stream<Arguments> addEmployeeToCompanyParameters() {
    return Stream.of(
        Arguments.of(
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setCompany(null)
                .setPhones(List.of(
                    new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company2 S.L"),
            """
                {
                  "cif": "B86017472",
                  "name": "Company2 S.L",
                  "employees":[
                    {
                    "nif": "45134320V",
                    "name": "Walter",
                    "surname":"Martín Lopes",
                    "birthYear": 1998,
                    "gender": "MALE",
                    "personalPhone": "+34722748406",
                    "company": "B86017472",
                    "email": "wmlopes0@gmail.com"
                    }
                  ]
                }
                """,
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setCompany("B86017472")
                .setPhones(List.of(
                    new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company2 S.L")
        )
    );
  }

  @ParameterizedTest
  @MethodSource("removeEmployeeFromCompanyParameters")
  @DisplayName("Remove employee from company successfully returns 200 code response")
  void removeEmployeeFromCompany(EmployeeEntity employeeEntity, CompanyEntity companyEntity, EmployeeEntity employeeEntityExpected,
      CompanyEntity companyEntityExpected) throws Exception {
    employeeRepository.save(employeeEntity);
    companyRepository.save(companyEntity);

    String nif = "45134320V";
    String jsonContent = """
        {
               "cif": "B86017472"
              }""";

    mockMvc.perform(put("/employees/removeToCompany/{nif}", nif)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isOk());

    Optional<EmployeeEntity> fetchEmployeeExpected = Optional.of(employeeEntityExpected);
    Optional<EmployeeEntity> fetchEmployeeResult = employeeRepository.findById(nif);

    Assertions.assertEquals(fetchEmployeeExpected, fetchEmployeeResult);

    Optional<CompanyEntity> fetchCompanyExpected = Optional.of(companyEntityExpected);
    Optional<CompanyEntity> fetchCompanyResult = companyRepository.findById("B86017472");

    Assertions.assertEquals(fetchCompanyExpected, fetchCompanyResult);
  }

  private static Stream<Arguments> removeEmployeeFromCompanyParameters() {
    return Stream.of(
        Arguments.of(
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setCompany("B86017472")
                .setPhones(List.of(
                    new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company2 S.L"),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setCompany(null)
                .setPhones(List.of(
                    new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company2 S.L"))
    );
  }

  @Test
  @DisplayName("Deleted employee by ID successfully returns 200 code response")
  void deleteEmployeeByIdTest() throws Exception {
    String nif = "45134320V";

    employeeRepository.save(new EmployeeEntity()
        .setNif(nif)
        .setName("Walter")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
        .setEmail("wmlopes0@gmail.com")
    );

    mockMvc.perform(delete("/employees/{nif}", nif))
        .andExpect(status().isOk());

    Optional<EmployeeEntity> fetchExpected = Optional.empty();
    Optional<EmployeeEntity> fetchResult = employeeRepository.findById(nif);

    Assertions.assertEquals(fetchExpected, fetchResult);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "45134320Z",
      "4513"
  }, nullValues = "null")
  @DisplayName("Invalid NIF should result in HTTP 500 InternalServerError")
  void deleteEmployeeByIdInvalidTest(String nif) throws Exception {
    String jsonContent = String.format("""
        {
         "nif": "%s"
        }""", nif);
    String expected = """
        {
         "nif":"Invalid NIF"
        }
        """;

    mockMvc.perform(delete("/employees/{nif}", nif)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
        .andExpect(status().isBadRequest())
        .andExpect(content().json(expected, false));
  }

  @Test
  @DisplayName("Delete employee by ID failed returns 404 code response.")
  void deleteEmployeeByIdNotFoundTest() throws Exception {
    String nif = "45134320V";
    mockMvc.perform(delete("/employees/{nif}", nif))
        .andExpect(status().isNotFound());
  }
}
