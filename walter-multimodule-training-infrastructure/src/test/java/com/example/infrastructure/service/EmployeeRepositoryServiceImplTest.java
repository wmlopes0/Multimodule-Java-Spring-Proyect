package com.example.infrastructure.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import com.example.domain.entity.Employee;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeUpdateVO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.mapper.EmployeeInfrastructureMapper;
import com.example.infrastructure.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeRepositoryServiceImplTest {

  @Mock
  private EmployeeRepository repository;

  @Mock
  private EmployeeInfrastructureMapper employeeInfrastructureMapper;

  @InjectMocks
  private EmployeeRepositoryServiceImpl service;

  @Test
  @DisplayName("Retrieve Employee list successfully")
  void listEmployeesTest() {
    EmployeeEntity employeeEntity1 = new EmployeeEntity(1L, "Walter");
    EmployeeEntity employeeEntity2 = new EmployeeEntity(2L, "Quique");
    Employee employee1 = new Employee(1L, "Walter");
    Employee employee2 = new Employee(2L, "Quique");

    Mockito.when(repository.findAll()).thenReturn(List.of(employeeEntity1, employeeEntity2));
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity1)).thenReturn(employee1);
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity2)).thenReturn(employee2);

    List<Employee> result = service.listEmployees();
    List<Employee> expected = List.of(employee1, employee2);

    Assertions.assertEquals(expected, result);
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(employeeInfrastructureMapper, atLeastOnce()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Retrieve empty Employee list when no employees present")
  void emptyListEmployeesTest() {
    Mockito.when(repository.findAll()).thenReturn(List.of());

    List<Employee> result = service.listEmployees();
    List<Employee> expected = List.of();

    Assertions.assertEquals(expected, result);
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("ListEmployees Throws RuntimeException on Error")
  void listEmployeesErrorTest() {
    Mockito.when(repository.findAll()).thenThrow(new RuntimeException("An error occurred"));
    Assertions.assertThrows(RuntimeException.class, () -> service.listEmployees());
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Retrieve Employee by ID successfully")
  void getEmployeeByIdTest() {
    Long id = 1L;
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(repository.findById(id)).thenReturn(Optional.of(employeeEntity));
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.getEmployeeById(id);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).findById(id);
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Employee Not Found by ID")
  void getEmployeeByIdNotFoundTest() {
    Long id = 1L;

    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    Employee result = service.getEmployeeById(id);

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).findById(id);
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("GetEmployeeById Throws RuntimeException on Error")
  void getEmployeeByIdErrorTest() {
    Long id = 1L;
    Mockito.when(repository.findById(id)).thenThrow(new RuntimeException("An error occurred"));
    Assertions.assertThrows(RuntimeException.class, () -> service.getEmployeeById(id));
    Mockito.verify(repository, times(1)).findById(id);
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Retrieve Employee by name successfully")
  void getEmployeeByNameTest() {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(repository.findFirstByNameContainingIgnoreCase(employeeNameVO.getName())).thenReturn(Optional.of(employeeEntity));
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.getEmployeeByName(employeeNameVO);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(employeeNameVO.getName());
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Employee Not Found by name")
  void getEmployeeByNameNotFoundTest() {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();

    Mockito.when(repository.findFirstByNameContainingIgnoreCase(employeeNameVO.getName())).thenReturn(Optional.empty());
    Employee result = service.getEmployeeByName(employeeNameVO);

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(employeeNameVO.getName());
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("GetEmployeeByName Throws RuntimeException on Error")
  void getEmployeeByNameErrorTest() {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();

    Mockito.when(repository.findFirstByNameContainingIgnoreCase(employeeNameVO.getName()))
        .thenThrow(new RuntimeException("An error occurred"));
    Assertions.assertThrows(RuntimeException.class, () -> service.getEmployeeByName(employeeNameVO));
    Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(employeeNameVO.getName());
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Add new Employee successfully")
  void addEmployeeTest() {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(employeeInfrastructureMapper.mapToEntity(employeeNameVO)).thenReturn(employeeEntity);
    Mockito.when(repository.save(employeeEntity)).thenReturn(employeeEntity);
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.addEmployee(employeeNameVO);

    Assertions.assertEquals(employee, result);
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToEntity(employeeNameVO);
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("AddEmployee Throws RuntimeException on Error")
  void addEmployeeErrorTest() {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");

    Mockito.when(employeeInfrastructureMapper.mapToEntity(employeeNameVO)).thenReturn(employeeEntity);
    Mockito.when(repository.save(employeeEntity)).thenThrow(new RuntimeException("An error occurred"));

    Assertions.assertThrows(RuntimeException.class, () -> service.addEmployee(employeeNameVO));
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToEntity(employeeNameVO);
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Update Employee information successfully")
  void updateEmployeeByIdTest() {
    EmployeeUpdateVO employeeUpdateVO = EmployeeUpdateVO.builder().number(1L).name("Walter").build();
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(repository.existsById(employeeUpdateVO.getNumber())).thenReturn(true);
    Mockito.when(employeeInfrastructureMapper.mapToEntity(employeeUpdateVO)).thenReturn(employeeEntity);
    Mockito.when(repository.save(employeeEntity)).thenReturn(employeeEntity);
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.updateEmployeeById(employeeUpdateVO);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).existsById(employeeUpdateVO.getNumber());
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToEntity(employeeUpdateVO);
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("UpdateEmployee Throws RuntimeException on Error")
  void updateEmployeeErrorTest() {
    EmployeeUpdateVO employeeUpdateVO = EmployeeUpdateVO.builder().number(1L).name("Walter").build();
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");

    Mockito.when(repository.existsById(employeeUpdateVO.getNumber())).thenReturn(true);
    Mockito.when(employeeInfrastructureMapper.mapToEntity(employeeUpdateVO)).thenReturn(employeeEntity);
    Mockito.when(repository.save(employeeEntity)).thenThrow(new RuntimeException("An error occurred"));

    Assertions.assertThrows(RuntimeException.class, () -> service.updateEmployeeById(employeeUpdateVO));

    Mockito.verify(repository, times(1)).existsById(employeeUpdateVO.getNumber());
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToEntity(employeeUpdateVO);
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Fail to update non-existent Employee")
  void updateEmployeeByIdNotFoundTest() {
    EmployeeUpdateVO employeeUpdateVO = EmployeeUpdateVO.builder().number(1L).name("Walter").build();

    Mockito.when(repository.existsById(employeeUpdateVO.getNumber())).thenReturn(false);
    Employee result = service.updateEmployeeById(employeeUpdateVO);

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).existsById(employeeUpdateVO.getNumber());
    Mockito.verify(employeeInfrastructureMapper, never()).mapToEntity(employeeUpdateVO);
    Mockito.verify(repository, never()).save(any(EmployeeEntity.class));
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
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
  @DisplayName("DeleteEmployeeById Throws RuntimeException on Error")
  void deleteEmployeeByIdErrorTest() {
    Long id = 1L;

    Mockito.when(repository.existsById(id)).thenReturn(true);
    Mockito.doThrow(new RuntimeException("An error occurred")).when(repository).deleteById(id);

    Assertions.assertThrows(RuntimeException.class, () -> service.deleteEmployeeById(id));

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