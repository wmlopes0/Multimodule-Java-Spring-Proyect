package com.example.application.employee.query.handler;

import static org.mockito.Mockito.times;

import com.example.application.employee.query.query.EmployeeByIdQuery;
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
class EmployeeGetByIdHandlerImplTest {

  @Mock
  private EmployeeService repositoryService;

  @InjectMocks
  private EmployeeGetByIdHandlerImpl employeeGetById;

  @Test
  @DisplayName("Retrieve Employee by ID successfully")
  void getEmployeeByIdTest() {
    EmployeeByIdQuery employeeByIdQuery = new EmployeeByIdQuery(1L);
    Employee expected = new Employee().setNumber(1L);

    Mockito.when(repositoryService.getEmployeeById(employeeByIdQuery.getNumber())).thenReturn(expected);
    Employee result = employeeGetById.getEmployeeById(employeeByIdQuery);

    Assertions.assertEquals(expected, result);
    Mockito.verify(repositoryService, times(1)).getEmployeeById(employeeByIdQuery.getNumber());
  }

  @Test
  @DisplayName("Employee Not Found by ID")
  void getEmployeeByIdNotFoundTest() {
    EmployeeByIdQuery employeeByIdQuery = new EmployeeByIdQuery(1L);

    Mockito.when(repositoryService.getEmployeeById(employeeByIdQuery.getNumber())).thenReturn(null);
    Employee result = employeeGetById.getEmployeeById(employeeByIdQuery);

    Assertions.assertNull(result);
    Mockito.verify(repositoryService, times(1)).getEmployeeById(employeeByIdQuery.getNumber());
  }
}