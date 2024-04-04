package com.example.contract.employee.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.cmd.handler.EmployeeCreateHandler;
import com.example.application.employee.cmd.handler.EmployeeDeleteHandler;
import com.example.application.employee.cmd.handler.EmployeeUpdateHandler;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.application.employee.query.handler.EmployeeGetByIdHandler;
import com.example.application.employee.query.handler.EmployeeGetByNameHandler;
import com.example.application.employee.query.handler.EmployeeListHandler;
import com.example.contract.employee.dto.EmployeeNameDTO;
import com.example.contract.employee.dto.EmployeeNameDetailsDTO;
import com.example.contract.employee.dto.EmployeeResponseDTO;
import com.example.contract.employee.mapper.EmployeeContractMapper;
import com.example.domain.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
  private EmployeeContractMapper mapper;

  @InjectMocks
  private EmployeeRestController controller;

  @Test
  @DisplayName("List employees return correctly list")
  void listEmployeesTest() {
    Employee employee1 = new Employee(1L, "Walter");
    Employee employee2 = new Employee(2L, "Quique");

    EmployeeNameDetailsDTO employeeDTO1 = new EmployeeNameDetailsDTO(employee1.getNumber(), employee1.getName().toUpperCase(),
        employee1.getName().length());
    EmployeeNameDetailsDTO employeeDTO2 = new EmployeeNameDetailsDTO(employee2.getNumber(), employee2.getName().toUpperCase(),
        employee2.getName().length());

    Mockito.when(employeeListHandler.listEmployees()).thenReturn(List.of(employee1, employee2));
    Mockito.when(mapper.mapToDetailsDTO(employee1)).thenReturn(employeeDTO1);
    Mockito.when(mapper.mapToDetailsDTO(employee2)).thenReturn(employeeDTO2);

    ResponseEntity<List<EmployeeNameDetailsDTO>> result = controller.listEmployees();
    ResponseEntity<List<EmployeeNameDetailsDTO>> expected = ResponseEntity.ok(List.of(employeeDTO1, employeeDTO2));

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(employeeListHandler, times(1)).listEmployees();
    Mockito.verify(mapper, atLeastOnce()).mapToDetailsDTO(any(Employee.class));
  }

  @Test
  @DisplayName("When list employees is empty return correctly list")
  void listEmployeesEmptyTest() {
    Mockito.when(employeeListHandler.listEmployees()).thenReturn(List.of());

    ResponseEntity<List<EmployeeNameDetailsDTO>> result = controller.listEmployees();
    ResponseEntity<List<EmployeeNameDetailsDTO>> expected = ResponseEntity.ok(List.of());

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(employeeListHandler, times(1)).listEmployees();
    Mockito.verify(mapper, never()).mapToDetailsDTO(any(Employee.class));
  }

  @Test
  @DisplayName("ListEmployees Throws RuntimeException on Repository Error")
  void listEmployeesErrorTest() {
    Mockito.when(employeeListHandler.listEmployees()).thenThrow(new RuntimeException("An error occurred"));
    Assertions.assertThrows(RuntimeException.class, () -> controller.listEmployees());
    Mockito.verify(employeeListHandler, times(1)).listEmployees();
    Mockito.verify(mapper, never()).mapToDetailsDTO(any(Employee.class));
  }

  @Test
  @DisplayName("Get employee by ID returns employee and 200 response correctly")
  void getEmployeeByIdTest() {
    Long id = 1L;
    Employee employee = new Employee(1L, "Walter");
    EmployeeByIdQuery employeeByIdQuery = new EmployeeByIdQuery(id);
    EmployeeNameDetailsDTO employeeDTO = new EmployeeNameDetailsDTO()
        .setNumber(employee.getNumber())
        .setName(employee.getName().toUpperCase())
        .setNameLength(employee.getName().length());

    Mockito.when(mapper.mapToEmployeeByIdQuery(id)).thenReturn(employeeByIdQuery);
    Mockito.when(employeeGetByIdHandler.getEmployeeById(employeeByIdQuery)).thenReturn(employee);
    Mockito.when(mapper.mapToDetailsDTO(employee)).thenReturn(employeeDTO);
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeById(id);
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.ok(employeeDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(mapper, times(1)).mapToEmployeeByIdQuery(id);
    Mockito.verify(employeeGetByIdHandler, times(1)).getEmployeeById(employeeByIdQuery);
    Mockito.verify(mapper, times(1)).mapToDetailsDTO(employee);
  }

  @Test
  @DisplayName("Get employee by ID not found returns 404 response")
  void getEmployeeByIdNotFoundTest() {
    Long id = 1L;
    EmployeeByIdQuery employeeByIdQuery = new EmployeeByIdQuery(id);

    Mockito.when(mapper.mapToEmployeeByIdQuery(id)).thenReturn(employeeByIdQuery);
    Mockito.when(employeeGetByIdHandler.getEmployeeById(employeeByIdQuery)).thenReturn(null);
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeById(id);
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.notFound().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(mapper, times(1)).mapToEmployeeByIdQuery(id);
    Mockito.verify(employeeGetByIdHandler, times(1)).getEmployeeById(employeeByIdQuery);
    Mockito.verify(mapper, never()).mapToDetailsDTO(any(Employee.class));
  }

  @Test
  @DisplayName("Get employee by name returns employee and 200 response correctly")
  void getEmployeeByNameTest() {
    String name = "Wal";
    EmployeeByNameQuery employeeByNameQuery = new EmployeeByNameQuery(name);
    Employee employee = new Employee(1L, "Walter");
    EmployeeNameDetailsDTO employeeDTO = new EmployeeNameDetailsDTO()
        .setNumber(employee.getNumber())
        .setName(employee.getName().toUpperCase())
        .setNameLength(employee.getName().length());

    Mockito.when(mapper.mapToEmployeeByNameQuery(name)).thenReturn(employeeByNameQuery);
    Mockito.when(employeeGetByNameHandler.getEmployeeByName(employeeByNameQuery)).thenReturn(employee);
    Mockito.when(mapper.mapToDetailsDTO(employee)).thenReturn(employeeDTO);
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeByName(name);
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.ok(employeeDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(mapper, times(1)).mapToEmployeeByNameQuery(name);
    Mockito.verify(employeeGetByNameHandler, times(1)).getEmployeeByName(employeeByNameQuery);
    Mockito.verify(mapper, times(1)).mapToDetailsDTO(employee);
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
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeByName(name);
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.notFound().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(mapper, times(1)).mapToEmployeeByNameQuery(name);
    Mockito.verify(employeeGetByNameHandler, times(1)).getEmployeeByName(employeeByNameQuery);
    Mockito.verify(mapper, never()).mapToDetailsDTO(any(Employee.class));
  }

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Add new employee returns 201 response")
  void newEmployeeTest(String name) {
    EmployeeNameDTO requestBody = new EmployeeNameDTO(name);
    EmployeeCreateCmd employeeCreateCmd = new EmployeeCreateCmd(requestBody.getName());
    Employee newEmployee = new Employee()
        .setName(requestBody.getName());
    EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO()
        .setNumber(newEmployee.getNumber())
        .setName(newEmployee.getName());

    Mockito.when(employeeCreateHandler.addEmployee(employeeCreateCmd)).thenReturn(newEmployee);
    Mockito.when(mapper.mapToResponseDTO(newEmployee)).thenReturn(employeeResponse);
    Mockito.when(mapper.mapToEmployeeCreateCmd(requestBody)).thenReturn(employeeCreateCmd);

    ResponseEntity<EmployeeResponseDTO> result = controller.newEmployee(requestBody);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.status(HttpStatus.CREATED).body(employeeResponse);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(employeeCreateHandler, times(1)).addEmployee(employeeCreateCmd);
    Mockito.verify(mapper, times(1)).mapToResponseDTO(any(Employee.class));
    Mockito.verify(mapper, times(1)).mapToEmployeeCreateCmd(any(EmployeeNameDTO.class));
  }

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Update employee by ID successfully returns 200 code response")
  void updateEmployeeByIdTest(String name) {
    Long id = 1L;
    EmployeeNameDTO requestBody = new EmployeeNameDTO(name);
    EmployeeUpdateCmd employeeUpdateCmd = new EmployeeUpdateCmd(id, requestBody.getName());
    Employee updatedEmployee = new Employee(id, requestBody.getName());
    EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO()
        .setNumber(updatedEmployee.getNumber())
        .setName(updatedEmployee.getName());

    Mockito.when(employeeUpdateHandler.updateEmployee(employeeUpdateCmd)).thenReturn(updatedEmployee);
    Mockito.when(mapper.mapToResponseDTO(updatedEmployee)).thenReturn(employeeResponse);
    Mockito.when(mapper.mapToEmployeeUpdateCmd(id, requestBody)).thenReturn(employeeUpdateCmd);

    ResponseEntity<EmployeeResponseDTO> result = controller.updateEmployeeById(id, requestBody);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(employeeResponse);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(employeeUpdateHandler, times(1)).updateEmployee(employeeUpdateCmd);
    Mockito.verify(mapper, times(1)).mapToResponseDTO(any(Employee.class));
    Mockito.verify(mapper, times(1)).mapToEmployeeUpdateCmd(id, requestBody);
  }

  @Test
  @DisplayName("Update employee by ID not found returns 404 code response")
  void updateEmployeeByIdNotFoundTest() {
    Long id = 1L;
    EmployeeNameDTO requestBody = new EmployeeNameDTO("Walter");
    EmployeeUpdateCmd employeeUpdateCmd = new EmployeeUpdateCmd(id, requestBody.getName());

    Mockito.when(employeeUpdateHandler.updateEmployee(employeeUpdateCmd)).thenReturn(null);
    Mockito.when(mapper.mapToEmployeeUpdateCmd(id, requestBody)).thenReturn(employeeUpdateCmd);

    Exception exception = Assertions.assertThrows(ResponseStatusException.class, () -> controller.updateEmployeeById(id, requestBody));
    Assertions.assertTrue(exception.getMessage().contains("Empleado no encontrado con ID: " + id));
    Mockito.verify(mapper, times(1)).mapToEmployeeUpdateCmd(id, requestBody);
    Mockito.verify(employeeUpdateHandler, times(1)).updateEmployee(employeeUpdateCmd);
    Mockito.verify(mapper, never()).mapToResponseDTO(any(Employee.class));
  }

  @ParameterizedTest
  @CsvSource(value = {
      "1",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Deleted employee by ID successfully returns 200 code response")
  void deleteEmployeeByIdTest(Long id) {
    EmployeeDeleteCmd employeeDeleteCmd = new EmployeeDeleteCmd(id);

    Mockito.when(mapper.mapToEmployeeDeleteCmd(id)).thenReturn(employeeDeleteCmd);
    Mockito.when(employeeDeleteHandler.deleteEmployee(employeeDeleteCmd)).thenReturn(true);
    ResponseEntity<Object> result = controller.deleteEmployeeById(id);
    ResponseEntity<Object> expected = ResponseEntity.ok().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Mockito.verify(mapper, times(1)).mapToEmployeeDeleteCmd(id);
    Mockito.verify(employeeDeleteHandler, times(1)).deleteEmployee(employeeDeleteCmd);
  }

  @Test
  @DisplayName("Delete employee by ID failed returns 404 code response")
  void deleteEmployeeByIdNotFoundTest() {
    Long id = 1L;
    EmployeeDeleteCmd employeeDeleteCmd = new EmployeeDeleteCmd(id);

    Mockito.when(mapper.mapToEmployeeDeleteCmd(id)).thenReturn(employeeDeleteCmd);
    Mockito.when(employeeDeleteHandler.deleteEmployee(employeeDeleteCmd)).thenReturn(false);
    ResponseEntity<Object> result = controller.deleteEmployeeById(id);
    ResponseEntity<Object> expected = ResponseEntity.notFound().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Mockito.verify(mapper, times(1)).mapToEmployeeDeleteCmd(id);
    Mockito.verify(employeeDeleteHandler, times(1)).deleteEmployee(employeeDeleteCmd);
  }
}