package com.example.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import com.example.cmd.EmployeeCreateCmd;
import com.example.cmd.EmployeeUpdateCmd;
import com.example.domain.Employee;
import com.example.entity.EmployeeEntity;
import com.example.mapper.EmployeeServiceMapperImpl;
import com.example.query.EmployeeByNameQuery;
import com.example.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

  @Mock
  private EmployeeRepository repository;

  @Mock
  private EmployeeServiceMapperImpl serviceMapper;

  @InjectMocks
  private EmployeeServiceImpl service;

  @Test
  @DisplayName("Retrieve Employee list successfully")
  void listEmployeesTest() {
    EmployeeEntity employeeEntity1 = new EmployeeEntity(1L, "Walter");
    EmployeeEntity employeeEntity2 = new EmployeeEntity(2L, "Quique");
    Employee employee1 = new Employee(1L, "Walter");
    Employee employee2 = new Employee(2L, "Quique");

    Mockito.when(repository.findAll()).thenReturn(List.of(employeeEntity1, employeeEntity2));
    Mockito.when(serviceMapper.mapToDomain(employeeEntity1)).thenReturn(employee1);
    Mockito.when(serviceMapper.mapToDomain(employeeEntity2)).thenReturn(employee2);

    List<Employee> result = service.listEmployees();
    List<Employee> expected = List.of(employee1, employee2);

    Assertions.assertEquals(expected, result);
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(serviceMapper, atLeastOnce()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Retrieve empty Employee list when no employees present")
  void emptyListEmployeesTest() {
    Mockito.when(repository.findAll()).thenReturn(List.of());

    List<Employee> result = service.listEmployees();
    List<Employee> expected = List.of();

    Assertions.assertEquals(expected, result);
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(serviceMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Retrieve Employee by ID successfully")
  void getEmployeeByIdTest() {
    Long id = 1L;
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(repository.findById(id)).thenReturn(Optional.of(employeeEntity));
    Mockito.when(serviceMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.getEmployeeById(id);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).findById(id);
    Mockito.verify(serviceMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Employee Not Found by ID")
  void getEmployeeByIdNotFoundTest() {
    Long id = 1L;

    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    Employee result = service.getEmployeeById(id);

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).findById(id);
    Mockito.verify(serviceMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Retrieve Employee by name successfully")
  void getEmployeeByNameTest() {
    EmployeeByNameQuery employeeByNameQuery = new EmployeeByNameQuery("Walter");
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(repository.findFirstByNameContainingIgnoreCase(employeeByNameQuery.getName())).thenReturn(Optional.of(employeeEntity));
    Mockito.when(serviceMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.getEmployeeByName(employeeByNameQuery);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(employeeByNameQuery.getName());
    Mockito.verify(serviceMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Employee Not Found by name")
  void getEmployeeByNameNotFoundTest() {
    EmployeeByNameQuery employeeByNameQuery = new EmployeeByNameQuery("Walter");

    Mockito.when(repository.findFirstByNameContainingIgnoreCase(employeeByNameQuery.getName())).thenReturn(Optional.empty());
    Employee result = service.getEmployeeByName(employeeByNameQuery);

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(employeeByNameQuery.getName());
    Mockito.verify(serviceMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Add new Employee successfully")
  void addEmployeeTest() {
    EmployeeCreateCmd employeeCreateCmd = new EmployeeCreateCmd("Walter");
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(serviceMapper.mapToEntity(employeeCreateCmd)).thenReturn(employeeEntity);
    Mockito.when(repository.save(employeeEntity)).thenReturn(employeeEntity);
    Mockito.when(serviceMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.addEmployee(employeeCreateCmd);

    Assertions.assertEquals(employee, result);
    Mockito.verify(serviceMapper, times(1)).mapToEntity(employeeCreateCmd);
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(serviceMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Update Employee information successfully")
  void updateEmployeeByIdTest() {
    EmployeeUpdateCmd employeeUpdateCmd = new EmployeeUpdateCmd(1L, "Walter");
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(repository.existsById(employeeUpdateCmd.getNumber())).thenReturn(true);
    Mockito.when(serviceMapper.mapToEntity(employeeUpdateCmd)).thenReturn(employeeEntity);
    Mockito.when(repository.save(employeeEntity)).thenReturn(employeeEntity);
    Mockito.when(serviceMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.updateEmployeeById(employeeUpdateCmd);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).existsById(employeeUpdateCmd.getNumber());
    Mockito.verify(serviceMapper, times(1)).mapToEntity(employeeUpdateCmd);
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(serviceMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Fail to update non-existent Employee")
  void updateEmployeeByIdNotFoundTest() {
    EmployeeUpdateCmd employeeUpdateCmd = new EmployeeUpdateCmd(1L, "Walter");

    Mockito.when(repository.existsById(employeeUpdateCmd.getNumber())).thenReturn(false);
    Employee result = service.updateEmployeeById(employeeUpdateCmd);

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).existsById(employeeUpdateCmd.getNumber());
    Mockito.verify(serviceMapper, never()).mapToEntity(employeeUpdateCmd);
    Mockito.verify(repository, never()).save(any(EmployeeEntity.class));
    Mockito.verify(serviceMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Delete Employee by ID successfully")
  void deleteEmployeeByIdTest() {
    Long id = 1L;

    Mockito.when(repository.existsById(id)).thenReturn(true);

    boolean result = service.deleteEmployeeById(id);

    Assertions.assertTrue(result);
    Mockito.verify(repository, times(1)).existsById(id);
    Mockito.verify(repository, times(1)).deleteById(id);
  }

  @Test
  @DisplayName("Employee deletion fails for non-existent ID")
  void deleteEmployeeByIdNotFoundTest() {
    Long id = 1L;

    Mockito.when(repository.existsById(id)).thenReturn(false);

    boolean result = service.deleteEmployeeById(id);

    Assertions.assertFalse(result);
    Mockito.verify(repository, times(1)).existsById(id);
    Mockito.verify(repository, never()).deleteById(any(Long.class));
  }
}