package com.example.application.employee.query.handler;

import static org.mockito.Mockito.times;

import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeListHandlerImplTest {

  @Mock
  private EmployeeService repositoryService;

  @InjectMocks
  private EmployeeListHandlerImpl employeeListImpl;

  @Test
  @DisplayName("Retrieve Employee list successfully")
  void listEmployeesTest() {
    Employee employee1 = new Employee(1L, "Walter");
    Employee employee2 = new Employee(1L, "Quique");
    List<Employee> expected = List.of(employee1, employee2);

    Mockito.when(repositoryService.listEmployees()).thenReturn(expected);
    List<Employee> result = employeeListImpl.listEmployees();

    Assertions.assertEquals(expected, result);
    Mockito.verify(repositoryService, times(1)).listEmployees();
  }

  @Test
  @DisplayName("Retrieve empty Employee list when no employees present")
  void emptyListEmployeesTest() {
    List<Employee> expected = List.of();

    Mockito.when(repositoryService.listEmployees()).thenReturn(expected);
    List<Employee> result = employeeListImpl.listEmployees();

    Assertions.assertEquals(expected, result);
    Mockito.verify(repositoryService, times(1)).listEmployees();
  }

  @Test
  @DisplayName("ListEmployees Throws Exception on Error")
  void listEmployeesErrorTest() {
    Mockito.when(repositoryService.listEmployees()).thenThrow(new Exception("An error occurred"));
    Assertions.assertThrows(RuntimeException.class, () -> employeeListImpl.listEmployees());
    Mockito.verify(repositoryService, times(1)).listEmployees();
  }
}