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
import com.example.infrastructure.mapper.EmployeeMapper;
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
  private EmployeeMapper employeeMapper;

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
    Mockito.when(employeeMapper.mapToDomain(employeeEntity1)).thenReturn(employee1);
    Mockito.when(employeeMapper.mapToDomain(employeeEntity2)).thenReturn(employee2);

    List<Employee> result = service.listEmployees();
    List<Employee> expected = List.of(employee1, employee2);

    Assertions.assertEquals(expected, result);
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(employeeMapper, atLeastOnce()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Retrieve empty Employee list when no employees present")
  void emptyListEmployeesTest() {
    Mockito.when(repository.findAll()).thenReturn(List.of());

    List<Employee> result = service.listEmployees();
    List<Employee> expected = List.of();

    Assertions.assertEquals(expected, result);
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(employeeMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Retrieve Employee by ID successfully")
  void getEmployeeByIdTest() {
    Long id = 1L;
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(repository.findById(id)).thenReturn(Optional.of(employeeEntity));
    Mockito.when(employeeMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.getEmployeeById(id);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).findById(id);
    Mockito.verify(employeeMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Employee Not Found by ID")
  void getEmployeeByIdNotFoundTest() {
    Long id = 1L;

    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    Employee result = service.getEmployeeById(id);

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).findById(id);
    Mockito.verify(employeeMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Retrieve Employee by name successfully")
  void getEmployeeByNameTest() {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(repository.findFirstByNameContainingIgnoreCase(employeeNameVO.getName())).thenReturn(Optional.of(employeeEntity));
    Mockito.when(employeeMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.getEmployeeByName(employeeNameVO);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(employeeNameVO.getName());
    Mockito.verify(employeeMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Employee Not Found by name")
  void getEmployeeByNameNotFoundTest() {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();

    Mockito.when(repository.findFirstByNameContainingIgnoreCase(employeeNameVO.getName())).thenReturn(Optional.empty());
    Employee result = service.getEmployeeByName(employeeNameVO);

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(employeeNameVO.getName());
    Mockito.verify(employeeMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("Add new Employee successfully")
  void addEmployeeTest() {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(employeeMapper.mapToEntity(employeeNameVO)).thenReturn(employeeEntity);
    Mockito.when(repository.save(employeeEntity)).thenReturn(employeeEntity);
    Mockito.when(employeeMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.addEmployee(employeeNameVO);

    Assertions.assertEquals(employee, result);
    Mockito.verify(employeeMapper, times(1)).mapToEntity(employeeNameVO);
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(employeeMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Update Employee information successfully")
  void updateEmployeeByIdTest() {
    EmployeeUpdateVO employeeUpdateVO = EmployeeUpdateVO.builder().number(1L).name("Walter").build();
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, "Walter");
    Employee employee = new Employee(1L, "Walter");

    Mockito.when(repository.existsById(employeeUpdateVO.getNumber())).thenReturn(true);
    Mockito.when(employeeMapper.mapToEntity(employeeUpdateVO)).thenReturn(employeeEntity);
    Mockito.when(repository.save(employeeEntity)).thenReturn(employeeEntity);
    Mockito.when(employeeMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.updateEmployeeById(employeeUpdateVO);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).existsById(employeeUpdateVO.getNumber());
    Mockito.verify(employeeMapper, times(1)).mapToEntity(employeeUpdateVO);
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(employeeMapper, times(1)).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Fail to update non-existent Employee")
  void updateEmployeeByIdNotFoundTest() {
    EmployeeUpdateVO employeeUpdateVO = EmployeeUpdateVO.builder().number(1L).name("Walter").build();

    Mockito.when(repository.existsById(employeeUpdateVO.getNumber())).thenReturn(false);
    Employee result = service.updateEmployeeById(employeeUpdateVO);

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).existsById(employeeUpdateVO.getNumber());
    Mockito.verify(employeeMapper, never()).mapToEntity(employeeUpdateVO);
    Mockito.verify(repository, never()).save(any(EmployeeEntity.class));
    Mockito.verify(employeeMapper, never()).mapToDomain(any(EmployeeEntity.class));
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