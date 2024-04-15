package com.example.application.employee.query.handler;

import static org.mockito.Mockito.times;

import com.example.application.employee.mapper.EmployeeApplicationMapper;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.EmployeeNameVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeGetByNameHandlerImplTest {

  @Mock
  private EmployeeService repositoryService;

  @Mock
  private EmployeeApplicationMapper mapper;

  @InjectMocks
  private EmployeeGetByNameHandlerImpl employeeGetByNameImpl;

  @Test
  @DisplayName("Retrieve Employee by name successfully")
  void getEmployeeByNameTest() {
    EmployeeByNameQuery employeeByNameQuery = new EmployeeByNameQuery("Walter");
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();
    Employee expected = new Employee()
        .setNif("45134320V")
        .setName("Walter")
        .setBirthYear(1998)
        .setGender(Gender.MALE)
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");

    Mockito.when(mapper.mapToEmployeeNameVO(employeeByNameQuery)).thenReturn(employeeNameVO);
    Mockito.when(repositoryService.getEmployeeByName(employeeNameVO)).thenReturn(expected);
    Employee result = employeeGetByNameImpl.getEmployeeByName(employeeByNameQuery);

    Assertions.assertEquals(expected, result);
    Mockito.verify(mapper, times(1)).mapToEmployeeNameVO(employeeByNameQuery);
    Mockito.verify(repositoryService, times(1)).getEmployeeByName(employeeNameVO);
  }

  @Test
  @DisplayName("Employee Not Found by name")
  void getEmployeeByNameNotFoundTest() {
    EmployeeByNameQuery employeeByNameQuery = new EmployeeByNameQuery("Walter");
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();

    Mockito.when(mapper.mapToEmployeeNameVO(employeeByNameQuery)).thenReturn(employeeNameVO);
    Mockito.when(repositoryService.getEmployeeByName(employeeNameVO)).thenReturn(null);
    Employee result = employeeGetByNameImpl.getEmployeeByName(employeeByNameQuery);

    Assertions.assertNull(result);
    Mockito.verify(mapper, times(1)).mapToEmployeeNameVO(employeeByNameQuery);
    Mockito.verify(repositoryService, times(1)).getEmployeeByName(employeeNameVO);
  }

  @Test
  @DisplayName("GetEmployeeByName Throws RuntimeException on Error")
  void getEmployeeByNameErrorTest() {
    EmployeeByNameQuery employeeByNameQuery = new EmployeeByNameQuery("Walter");
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();

    Mockito.when(mapper.mapToEmployeeNameVO(employeeByNameQuery)).thenReturn(employeeNameVO);
    Mockito.when(repositoryService.getEmployeeByName(employeeNameVO)).thenThrow(new RuntimeException("An error occurred"));

    Assertions.assertThrows(RuntimeException.class, () -> employeeGetByNameImpl.getEmployeeByName(employeeByNameQuery));
    Mockito.verify(mapper, times(1)).mapToEmployeeNameVO(employeeByNameQuery);
    Mockito.verify(repositoryService, times(1)).getEmployeeByName(employeeNameVO);
  }
}