package com.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;

import com.example.cmd.EmployeeCreateCmd;
import com.example.cmd.EmployeeUpdateCmd;
import com.example.domain.Employee;
import com.example.dto.EmployeeNameDTO;
import com.example.dto.EmployeeNameDetailsDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.entity.EmployeeEntity;
import com.example.mapper.EmployeeControllerMapperImpl;
import com.example.query.EmployeeByNameQuery;
import com.example.service.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
  private EmployeeServiceImpl service;

  @Mock
  private EmployeeControllerMapperImpl controllerMapper;

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

    Mockito.when(service.listEmployees()).thenReturn(List.of(employee1, employee2));
    Mockito.when(controllerMapper.mapToDetailsDTO(employee1)).thenReturn(employeeDTO1);
    Mockito.when(controllerMapper.mapToDetailsDTO(employee2)).thenReturn(employeeDTO2);

    ResponseEntity<List<EmployeeNameDetailsDTO>> result = controller.listEmployees();
    ResponseEntity<List<EmployeeNameDetailsDTO>> expected = ResponseEntity.ok(List.of(employeeDTO1, employeeDTO2));

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(service, times(1)).listEmployees();
    Mockito.verify(controllerMapper, atLeastOnce()).mapToDetailsDTO(any(Employee.class));
  }

  @Test
  @DisplayName("When list employees is empty return correctly list")
  void listEmployeesEmptyTest() {
    Mockito.when(service.listEmployees()).thenReturn(List.of());

    ResponseEntity<List<EmployeeNameDetailsDTO>> result = controller.listEmployees();
    ResponseEntity<List<EmployeeNameDetailsDTO>> expected = ResponseEntity.ok(List.of());

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(service, times(1)).listEmployees();
    Mockito.verify(controllerMapper, never()).mapToDetailsDTO(any(Employee.class));
  }

  @Test
  @DisplayName("ListEmployees Throws RuntimeException on Repository Error")
  void listEmployeesErrorTest() {
    Mockito.when(service.listEmployees()).thenThrow(new RuntimeException("An error occurred"));
    Assertions.assertThrows(RuntimeException.class, () -> controller.listEmployees());
    Mockito.verify(service, times(1)).listEmployees();
  }

  @Test
  @DisplayName("Get employee by ID returns employee and 200 response correctly")
  void getEmployeeByIdTest() {
    Long id = 1L;
    Employee employee = new Employee(1L, "Walter");
    EmployeeNameDetailsDTO employeeDTO = new EmployeeNameDetailsDTO()
        .setNumber(employee.getNumber())
        .setName(employee.getName().toUpperCase())
        .setNameLength(employee.getName().length());

    Mockito.when(service.getEmployeeById(id)).thenReturn(employee);
    Mockito.when(controllerMapper.mapToDetailsDTO(employee)).thenReturn(employeeDTO);
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeById(id);
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.ok(employeeDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(service, times(1)).getEmployeeById(id);
    Mockito.verify(controllerMapper, times(1)).mapToDetailsDTO(employee);
  }

  @Test
  @DisplayName("Get employee by ID not found returns 404 response")
  void getEmployeeByIdNotFoundTest() {
    Long id = 1L;

    Mockito.when(service.getEmployeeById(id)).thenReturn(null);
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeById(id);
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.notFound().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(service, times(1)).getEmployeeById(id);
    Mockito.verify(controllerMapper, never()).mapToDetailsDTO(any(Employee.class));
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

    Mockito.when(service.getEmployeeByName(employeeByNameQuery)).thenReturn(employee);
    Mockito.when(controllerMapper.mapToDetailsDTO(employee)).thenReturn(employeeDTO);
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeByName(name);
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.ok(employeeDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(service, times(1)).getEmployeeByName(employeeByNameQuery);
    Mockito.verify(controllerMapper, times(1)).mapToDetailsDTO(employee);
  }

  @Test
  @DisplayName("Get employee by name not found returns 404 response")
  void getEmployeeByNameNotFoundTest() {
    String name = "Wal";
    EmployeeByNameQuery employeeByNameQuery = new EmployeeByNameQuery(name);

    Mockito.when(service.getEmployeeByName(employeeByNameQuery)).thenReturn(null);
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeByName(name);
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.notFound().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(service, times(1)).getEmployeeByName(employeeByNameQuery);
    Mockito.verify(controllerMapper, never()).mapToDetailsDTO(any(Employee.class));
  }

  @Test
  @DisplayName("Add new employee returns 201 response")
  void newEmployeeTest() {
    EmployeeNameDTO requestBody = new EmployeeNameDTO("Walter");
    EmployeeCreateCmd employeeCreateCmd = new EmployeeCreateCmd(requestBody.getName());
    Employee newEmployee = new Employee()
        .setName(requestBody.getName());
    EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO()
        .setNumber(newEmployee.getNumber())
        .setName(newEmployee.getName());

    Mockito.when(service.addEmployee(employeeCreateCmd)).thenReturn(newEmployee);
    Mockito.when(controllerMapper.mapToResponseDTO(newEmployee)).thenReturn(employeeResponse);
    Mockito.when(controllerMapper.mapToEmployeeCreateCmd(requestBody)).thenReturn(employeeCreateCmd);

    ResponseEntity<EmployeeResponseDTO> result = controller.newEmployee(requestBody);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.status(HttpStatus.CREATED).body(employeeResponse);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(service, times(1)).addEmployee(employeeCreateCmd);
    Mockito.verify(controllerMapper, times(1)).mapToResponseDTO(any(Employee.class));
    Mockito.verify(controllerMapper, times(1)).mapToEmployeeCreateCmd(any(EmployeeNameDTO.class));
  }

  @Test
  @DisplayName("Update employee by ID successfully returns 200 code response")
  void updateEmployeeByIdTest() {
    Long id = 1L;
    EmployeeNameDTO requestBody = new EmployeeNameDTO("Walter");
    EmployeeUpdateCmd employeeUpdateCmd = new EmployeeUpdateCmd(id, requestBody.getName());
    Employee updatedEmployee = new Employee(id, requestBody.getName());
    EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO()
        .setNumber(updatedEmployee.getNumber())
        .setName(updatedEmployee.getName());

    Mockito.when(service.updateEmployeeById(employeeUpdateCmd)).thenReturn(updatedEmployee);
    Mockito.when(controllerMapper.mapToResponseDTO(updatedEmployee)).thenReturn(employeeResponse);
    Mockito.when(controllerMapper.mapToEmployeeUpdateCmd(id, requestBody)).thenReturn(employeeUpdateCmd);

    ResponseEntity<EmployeeResponseDTO> result = controller.updateEmployeeById(id, requestBody);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(employeeResponse);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(service, times(1)).updateEmployeeById(employeeUpdateCmd);
    Mockito.verify(controllerMapper, times(1)).mapToResponseDTO(any(Employee.class));
    Mockito.verify(controllerMapper, times(1)).mapToEmployeeUpdateCmd(id, requestBody);
  }

  @Test
  @DisplayName("Update employee by ID not found returns 404 code response")
  void updateEmployeeByIdNotFoundTest() {
    Long id = 1L;
    EmployeeNameDTO requestBody = new EmployeeNameDTO("Walter");
    EmployeeUpdateCmd employeeUpdateCmd = new EmployeeUpdateCmd(id, requestBody.getName());

    Mockito.when(service.updateEmployeeById(employeeUpdateCmd)).thenReturn(null);
    Mockito.when(controllerMapper.mapToEmployeeUpdateCmd(id, requestBody)).thenReturn(employeeUpdateCmd);

    Exception exception = Assertions.assertThrows(ResponseStatusException.class, () -> controller.updateEmployeeById(id, requestBody));
    Assertions.assertTrue(exception.getMessage().contains("Empleado no encontrado con ID: " + id));
    Mockito.verify(controllerMapper, times(1)).mapToEmployeeUpdateCmd(id, requestBody);
    Mockito.verify(service, times(1)).updateEmployeeById(employeeUpdateCmd);
    Mockito.verify(controllerMapper, never()).mapToResponseDTO(any(Employee.class));
  }

  @Test
  @DisplayName("Deleted employee by ID successfully returns 200 code response")
  void deleteEmployeeByIdTest() {
    Long id = 1L;
    EmployeeEntity existingEmployeeEntity = new EmployeeEntity(id, "Walter");

    Mockito.when(service.deleteEmployeeById(id)).thenReturn(true);
    ResponseEntity<Object> result = controller.deleteEmployeeById(id);
    ResponseEntity<Object> expected = ResponseEntity.ok().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Mockito.verify(service, times(1)).deleteEmployeeById(id);
  }

  @Test
  @DisplayName("Delete employee by ID failed returns 404 code response")
  void deleteEmployeeByIdNotFoundTest() {
    Long id = 1L;

    Mockito.when(service.deleteEmployeeById(id)).thenReturn(false);
    ResponseEntity<Object> result = controller.deleteEmployeeById(id);
    ResponseEntity<Object> expected = ResponseEntity.notFound().build();

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Mockito.verify(service, times(1)).deleteEmployeeById(id);
  }

}