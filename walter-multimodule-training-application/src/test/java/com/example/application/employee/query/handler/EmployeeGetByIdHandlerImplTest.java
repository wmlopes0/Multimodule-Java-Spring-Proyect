package com.example.application.employee.query.handler;

import static org.mockito.Mockito.times;

import com.example.application.employee.mapper.EmployeeApplicationMapper;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.EmployeeNifVO;
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

  @Mock
  private EmployeeApplicationMapper mapper;

  @InjectMocks
  private EmployeeGetByIdHandlerImpl employeeGetById;

  @Test
  @DisplayName("Retrieve Employee by ID successfully")
  void getEmployeeByIdTest() {
    EmployeeByIdQuery employeeByIdQuery = new EmployeeByIdQuery("45134320V");
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder()
        .nif("45134320V")
        .build();
    Employee expected = new Employee()
        .setNif("45134320V")
        .setName("Walter")
        .setBirthYear(1998)
        .setGender(Gender.MALE)
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");

    Mockito.when(mapper.mapToEmployeeNifVO(employeeByIdQuery)).thenReturn(employeeNifVO);
    Mockito.when(repositoryService.getEmployeeById(employeeNifVO)).thenReturn(expected);
    Employee result = employeeGetById.getEmployeeById(employeeByIdQuery);

    Assertions.assertEquals(expected, result);
    Mockito.verify(mapper, times(1)).mapToEmployeeNifVO(employeeByIdQuery);
    Mockito.verify(repositoryService, times(1)).getEmployeeById(employeeNifVO);
  }

  @Test
  @DisplayName("Employee Not Found by ID")
  void getEmployeeByIdNotFoundTest() {
    EmployeeByIdQuery employeeByIdQuery = new EmployeeByIdQuery("45134320V");
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder()
        .nif("45134320V")
        .build();

    Mockito.when(mapper.mapToEmployeeNifVO(employeeByIdQuery)).thenReturn(employeeNifVO);
    Mockito.when(repositoryService.getEmployeeById(employeeNifVO)).thenReturn(null);
    Employee result = employeeGetById.getEmployeeById(employeeByIdQuery);

    Assertions.assertNull(result);
    Mockito.verify(mapper, times(1)).mapToEmployeeNifVO(employeeByIdQuery);
    Mockito.verify(repositoryService, times(1)).getEmployeeById(employeeNifVO);
  }

  @Test
  @DisplayName("GetEmployeeById Throws RuntimeException on Error")
  void getEmployeeByIdErrorTest() {
    EmployeeByIdQuery employeeByIdQuery = new EmployeeByIdQuery("45134320V");
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder()
        .nif("45134320V")
        .build();

    Mockito.when(mapper.mapToEmployeeNifVO(employeeByIdQuery)).thenReturn(employeeNifVO);
    Mockito.when(repositoryService.getEmployeeById(employeeNifVO)).thenThrow(new RuntimeException("An error occurred"));

    Assertions.assertThrows(RuntimeException.class, () -> employeeGetById.getEmployeeById(employeeByIdQuery));
    Mockito.verify(mapper, times(1)).mapToEmployeeNifVO(employeeByIdQuery);
    Mockito.verify(repositoryService, times(1)).getEmployeeById(employeeNifVO);
  }
}