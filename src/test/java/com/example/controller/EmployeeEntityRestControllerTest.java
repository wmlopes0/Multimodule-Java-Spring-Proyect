package com.example.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import com.example.dto.EmployeeNameDTO;
import com.example.dto.EmployeeNameDetailsDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.entity.EmployeeEntity;
import com.example.mapper.EmployeeMapper;
import com.example.mapper.EmployeeResponseMapper;
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

@ExtendWith(MockitoExtension.class)
class EmployeeEntityRestControllerTest {

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
    EmployeeEntity employeeEntity1 = new EmployeeEntity(1L, "Walter");
    EmployeeEntity employeeEntity2 = new EmployeeEntity(2L, "Quique");
    EmployeeNameDetailsDTO employeeDTO1 = new EmployeeNameDetailsDTO(employeeEntity1.getNumber(), employeeEntity1.getName().toUpperCase(),
        employeeEntity1.getName().length());
    EmployeeNameDetailsDTO employeeDTO2 = new EmployeeNameDetailsDTO(employeeEntity2.getNumber(), employeeEntity2.getName().toUpperCase(),
        employeeEntity2.getName().length());

    Mockito.when(repository.findAll()).thenReturn(List.of(employeeEntity1, employeeEntity2));
    Mockito.when(employeeMapper.mapToDetailsDTO(employeeEntity1)).thenReturn(employeeDTO1);
    Mockito.when(employeeMapper.mapToDetailsDTO(employeeEntity2)).thenReturn(employeeDTO2);

    ResponseEntity<List<EmployeeNameDetailsDTO>> result = controller.listEmployees();
    ResponseEntity<List<EmployeeNameDetailsDTO>> expected = ResponseEntity.ok(List.of(employeeDTO1, employeeDTO2));

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(employeeMapper, atLeastOnce()).mapToDetailsDTO(any(EmployeeEntity.class));
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
    Mockito.verify(employeeMapper, never()).mapToDetailsDTO(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("ListEmployees Throws RuntimeException on Repository Error")
  void listEmployeesErrorTest() {
    Mockito.when(repository.findAll()).thenThrow(new RuntimeException("An error occurred"));
    Assertions.assertThrows(RuntimeException.class, () -> controller.listEmployees());
    Mockito.verify(repository, times(1)).findAll();
  }

  @Test
  @DisplayName("Get employee by ID returns employee and 200 response correctly")
  void getEmployeeByIdTest() {
    Long id = 1L;
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    EmployeeNameDetailsDTO employeeDTO = new EmployeeNameDetailsDTO()
        .setNumber(employeeEntity.getNumber())
        .setName(employeeEntity.getName().toUpperCase())
        .setNameLength(employeeEntity.getName().length());

    Mockito.when(repository.findById(id)).thenReturn(Optional.of(employeeEntity));
    Mockito.when(employeeMapper.mapToDetailsDTO(employeeEntity)).thenReturn(employeeDTO);
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeById(id);
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.ok(employeeDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(repository, times(1)).findById(id);
    Mockito.verify(employeeMapper, times(1)).mapToDetailsDTO(employeeEntity);
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
    Mockito.verify(employeeMapper, never()).mapToDetailsDTO(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Get employee by name returns employee and 200 response correctly")
  void getEmployeeByNameTest() {
    String name = "Wal";
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    EmployeeNameDetailsDTO employeeDTO = new EmployeeNameDetailsDTO()
        .setNumber(employeeEntity.getNumber())
        .setName(employeeEntity.getName().toUpperCase())
        .setNameLength(employeeEntity.getName().length());

    Mockito.when(repository.findFirstByNameContainingIgnoreCase(name)).thenReturn(Optional.of(employeeEntity));
    Mockito.when(employeeMapper.mapToDetailsDTO(employeeEntity)).thenReturn(employeeDTO);
    ResponseEntity<EmployeeNameDetailsDTO> result = controller.getEmployeeByName(name);
    ResponseEntity<EmployeeNameDetailsDTO> expected = ResponseEntity.ok(employeeDTO);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(name);
    Mockito.verify(employeeMapper, times(1)).mapToDetailsDTO(employeeEntity);
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
    Mockito.verify(employeeMapper, never()).mapToDetailsDTO(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Add new employee returns 201 response")
  void newEmployeeTest() {
    EmployeeNameDTO requestBody = new EmployeeNameDTO("Walter");
    EmployeeEntity newEmployeeEntity = new EmployeeEntity()
        .setName(requestBody.getName());
    EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO()
        .setNumber(newEmployeeEntity.getNumber())
        .setName(newEmployeeEntity.getName());

    Mockito.when(repository.save(newEmployeeEntity)).thenReturn(newEmployeeEntity);
    Mockito.when(employeeResponseMapper.toResponseDTO(newEmployeeEntity)).thenReturn(employeeResponse);

    ResponseEntity<EmployeeResponseDTO> result = controller.newEmployee(requestBody);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.status(HttpStatus.CREATED).body(employeeResponse);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(repository, times(1)).save(newEmployeeEntity);
    Mockito.verify(employeeResponseMapper, times(1)).toResponseDTO(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Update employee by ID successfully returns 200 code response")
  void updateEmployeeByIdTest() {
    Long id = 1L;
    EmployeeNameDTO requestBody = new EmployeeNameDTO("Walter");
    EmployeeEntity existingEmployeeEntity = new EmployeeEntity(id, "Quique");
    EmployeeEntity updatedEmployeeEntity = new EmployeeEntity(id, requestBody.getName());
    EmployeeResponseDTO employeeResponse = new EmployeeResponseDTO()
        .setNumber(updatedEmployeeEntity.getNumber())
        .setName(updatedEmployeeEntity.getName());

    Mockito.when(repository.findById(id)).thenReturn(Optional.of(existingEmployeeEntity));
    Mockito.when(repository.save(updatedEmployeeEntity)).thenReturn(updatedEmployeeEntity);
    Mockito.when(employeeResponseMapper.toResponseDTO(updatedEmployeeEntity)).thenReturn(employeeResponse);

    ResponseEntity<EmployeeResponseDTO> result = controller.updateEmployeeById(id, requestBody);
    ResponseEntity<EmployeeResponseDTO> expected = ResponseEntity.ok(employeeResponse);

    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
    Mockito.verify(repository, times(1)).findById(id);
    Mockito.verify(repository, times(1)).save(updatedEmployeeEntity);
    Mockito.verify(employeeResponseMapper, times(1)).toResponseDTO(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Update employee by ID not found returns 404 code response")
  void updateEmployeeByIdNotFoundTest() {
    Long id = 1L;
    EmployeeNameDTO requestBody = new EmployeeNameDTO("Walter");

    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

    Exception exception = Assertions.assertThrows(ResponseStatusException.class, () -> controller.updateEmployeeById(id, requestBody));
    Assertions.assertTrue(exception.getMessage().contains("Empleado no encontrado con ID: " + id));
    Mockito.verify(repository, times(1)).findById(id);
    Mockito.verify(repository, times(0)).save(any(EmployeeEntity.class));
    Mockito.verify(employeeResponseMapper, never()).toResponseDTO(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Deleted employee by ID successfully returns 200 code response")
  void deletedEmployeeByIdTest() {
    Long id = 1L;
    EmployeeEntity existingEmployeeEntity = new EmployeeEntity(id, "Walter");

    Mockito.when(repository.findById(id)).thenReturn(Optional.of(existingEmployeeEntity));
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