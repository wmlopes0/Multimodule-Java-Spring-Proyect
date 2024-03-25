package com.example.controller;

import com.example.mapper.EmployeeMapper;
import com.example.mapper.EmployeeResponseMapper;
import com.example.model.Employee;
import com.example.model.EmployeeDTO;
import com.example.model.EmployeeNameDetailsDTO;
import com.example.model.EmployeeResponseDTO;
import com.example.repository.EmployeeRepository;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeRestControllerTest {

    @Mock
    private EmployeeRepository repository;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private EmployeeResponseMapper employeeResponseMapper;
    @InjectMocks
    private EmployeeRestController controller;

    @Test
    @DisplayName("List employees return correctly list")
    void listEmployeesTest() {
        Employee employee1 = new Employee(1L, "Walter");
        Employee employee2 = new Employee(2L, "Quique");
        EmployeeNameDetailsDTO employeeDTO1 = new EmployeeNameDetailsDTO(employee1.getNumber(), employee1.getName().toUpperCase(), employee1.getName().length());
        EmployeeNameDetailsDTO employeeDTO2 = new EmployeeNameDetailsDTO(employee2.getNumber(), employee2.getName().toUpperCase(), employee2.getName().length());

        Mockito.when(repository.findAll()).thenReturn(List.of(employee1, employee2));
        Mockito.when(employeeMapper.toDetailsDTO(employee1)).thenReturn(employeeDTO1);
        Mockito.when(employeeMapper.toDetailsDTO(employee2)).thenReturn(employeeDTO2);

        ResponseEntity<List<EmployeeNameDetailsDTO>> result = controller.listEmployees();
        ResponseEntity<List<EmployeeNameDetailsDTO>> expected = ResponseEntity.ok(List.of(employeeDTO1, employeeDTO2));

        Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        Assertions.assertEquals(expected.getBody(), result.getBody());
        Mockito.verify(repository, times(1)).findAll();
        Mockito.verify(employeeMapper, atLeastOnce()).toDetailsDTO(any(Employee.class));
    }

    @Test
    @DisplayName("When list employees is empty return correctly list")
    void listEmployeesEmptyTest() {
        Mockito.when(repository.findAll()).thenReturn(List.of());

        ResponseEntity<List<EmployeeNameDetailsDTO>> result = controller.listEmployees();
        ResponseEntity<List<EmployeeNameDetailsDTO>> expected = ResponseEntity.ok(List.of());

        Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        Assertions.assertEquals(expected.getBody(), result.getBody());
        Mockito.verify(repository, times(1)).findAll();
        Mockito.verify(employeeMapper, never()).toDetailsDTO(any(Employee.class));
    }

    @Test
    @DisplayName("ListEmployees Throws RuntimeException on Repository Error")
    void listEmployeesErrorTest() {
        Mockito.when(repository.findAll()).thenThrow(new RuntimeException("An error ocurred"));
        Assertions.assertThrows(RuntimeException.class, () -> controller.listEmployees());
        Mockito.verify(repository, times(1)).findAll();
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

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(employee));
        Mockito.when(employeeMapper.toDetailsDTO(employee)).thenReturn(employeeDTO);
        ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeById(id);
        ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.ok(employeeDTO);

        Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        Assertions.assertEquals(expected.getBody(), result.getBody());
        Mockito.verify(repository, times(1)).findById(id);
        Mockito.verify(employeeMapper, times(1)).toDetailsDTO(employee);
    }

    @Test
    @DisplayName("Get employee by ID not found returns 404 response")
    void getEmployeeByIdNotFoundTest() {
        Long id = 1L;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeById(id);
        ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.notFound().build();

        Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        Assertions.assertEquals(expected.getBody(), result.getBody());
        Mockito.verify(repository, times(1)).findById(id);
        Mockito.verify(employeeMapper, never()).toDetailsDTO(any(Employee.class));
    }

    @Test
    @DisplayName("Get employee by name returns employee and 200 response correctly")
    void getEmployeeByNameTest() {
        String name = "Wal";
        Employee employee = new Employee(1L, "Walter");
        EmployeeNameDetailsDTO employeeDTO = new EmployeeNameDetailsDTO()
                .setNumber(employee.getNumber())
                .setName(employee.getName().toUpperCase())
                .setNameLength(employee.getName().length());

        Mockito.when(repository.findFirstByNameContainingIgnoreCase(name)).thenReturn(Optional.of(employee));
        Mockito.when(employeeMapper.toDetailsDTO(employee)).thenReturn(employeeDTO);
        ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeByName(name);
        ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.ok(employeeDTO);

        Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        Assertions.assertEquals(expected.getBody(), result.getBody());
        Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(name);
        Mockito.verify(employeeMapper, times(1)).toDetailsDTO(employee);
    }

    @Test
    @DisplayName("Get employee by name not found returns 404 response")
    void getEmployeeByNameNotFoundTest() {
        String name = "Wal";

        Mockito.when(repository.findFirstByNameContainingIgnoreCase(name)).thenReturn(Optional.empty());
        ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeByName(name);
        ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.notFound().build();

        Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        Assertions.assertEquals(expected.getBody(), result.getBody());
        Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(name);
        Mockito.verify(employeeMapper, never()).toDetailsDTO(any(Employee.class));
    }

    @Test
    @DisplayName("Add new employee returns 201 response")
    void newEmployeeTest() {
        EmployeeDTO requestBody = new EmployeeDTO("Walter");
        Employee newEmployee = new Employee()
                .setName(requestBody.getName());
        EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO()
                .setNumber(newEmployee.getNumber())
                .setName(newEmployee.getName());

        Mockito.when(repository.save(newEmployee)).thenReturn(newEmployee);
        Mockito.when(employeeResponseMapper.toResponseDTO(newEmployee)).thenReturn(employeeResponse);

        ResponseEntity<EmployeeResponseDTO> result = controller.newEmployee(requestBody);
        ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.status(HttpStatus.CREATED).body(employeeResponse);

        Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        Assertions.assertEquals(expected.getBody(), result.getBody());
        Mockito.verify(repository, times(1)).save(newEmployee);
        Mockito.verify(employeeResponseMapper, times(1)).toResponseDTO(any(Employee.class));
    }

    @Test
    @DisplayName("Update employee by ID successfully returns 200 code response")
    void updateEmployeeByIdTest() {
        Long id = 1L;
        EmployeeDTO requestBody = new EmployeeDTO("Walter");
        Employee existingEmployee = new Employee(id, "Quique");
        Employee updatedEmployee = new Employee(id, requestBody.getName());
        EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO()
                .setNumber(updatedEmployee.getNumber())
                .setName(updatedEmployee.getName());

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(existingEmployee));
        Mockito.when(repository.save(updatedEmployee)).thenReturn(updatedEmployee);
        Mockito.when(employeeResponseMapper.toResponseDTO(updatedEmployee)).thenReturn(employeeResponse);

        ResponseEntity<EmployeeResponseDTO> result = controller.updateEmployeeById(id, requestBody);
        ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(employeeResponse);

        Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        Assertions.assertEquals(expected.getBody(), result.getBody());
        Mockito.verify(repository, times(1)).findById(id);
        Mockito.verify(repository, times(1)).save(updatedEmployee);
        Mockito.verify(employeeResponseMapper, times(1)).toResponseDTO(any(Employee.class));
    }

    @Test
    @DisplayName("Update employee by ID not found returns 404 code response")
    void updateEmployeeByIdNotFoundTest() {
        Long id = 1L;
        EmployeeDTO requestBody = new EmployeeDTO("Walter");

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(ResponseStatusException.class, () -> controller.updateEmployeeById(id, requestBody));
        Assertions.assertTrue(exception.getMessage().contains("Empleado no encontrado con ID: " + id));
        Mockito.verify(repository, times(1)).findById(id);
        Mockito.verify(repository, times(0)).save(any(Employee.class));
        Mockito.verify(employeeResponseMapper, never()).toResponseDTO(any(Employee.class));
    }

    @Test
    @DisplayName("Deleted employee by ID successfully returns 200 code response")
    void deletedEmployeeByIdTest() {
        Long id = 1L;
        Employee existingEmployee = new Employee(id, "Walter");

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(existingEmployee));
        ResponseEntity<Object> result = controller.deletedEmployeeById(id);
        ResponseEntity<Object> expected = ResponseEntity.ok().build();

        Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        Mockito.verify(repository, times(1)).findById(id);
        Mockito.verify(repository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Delete employee by ID failed returns 404 code response")
    void deletedEmployeeByIdNotFoundTest() {
        Long id = 1L;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Object> result = controller.deletedEmployeeById(id);
        ResponseEntity<Object> expected = ResponseEntity.notFound().build();

        Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
        Mockito.verify(repository, times(1)).findById(id);
        Mockito.verify(repository, times(0)).deleteById(any(Long.class));
    }

}