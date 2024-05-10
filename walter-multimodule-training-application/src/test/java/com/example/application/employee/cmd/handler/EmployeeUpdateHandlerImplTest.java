package com.example.application.employee.cmd.handler;

import static org.mockito.Mockito.times;

import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.mapper.EmployeeApplicationMapper;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.employee.EmployeeVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeUpdateHandlerImplTest {

  @Mock
  private EmployeeService repositoryService;

  @Mock
  private EmployeeApplicationMapper mapper;

  @InjectMocks
  private EmployeeUpdateHandlerImpl employeeCreateImpl;

  @Test
  @DisplayName("Update Employee information successfully")
  void updateEmployeeTest() {
    EmployeeUpdateCmd employeeUpdateCmd = new EmployeeUpdateCmd()
        .setNif("45134320V")
        .setName("Walter")
        .setBirthYear(1998)
        .setGender("male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");
    EmployeeVO employeeVO = EmployeeVO.builder()
        .nif("45134320V")
        .name("Walter")
        .birthYear(1998)
        .gender(Gender.MALE)
        .personalPhone("+34722748406")
        .email("wmlopes0@gmail.com")
        .build();
    Employee employee = new Employee()
        .setNif("45134320V")
        .setName("Walter")
        .setBirthYear(1998)
        .setGender(Gender.MALE)
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");

    Mockito.when(mapper.mapToEmployeeVO(employeeUpdateCmd)).thenReturn(employeeVO);
    Mockito.when(repositoryService.updateEmployeeById(employeeVO)).thenReturn(employee);
    Employee result = employeeCreateImpl.updateEmployee(employeeUpdateCmd);

    Assertions.assertEquals(employee, result);
    Mockito.verify(mapper, times(1)).mapToEmployeeVO(employeeUpdateCmd);
    Mockito.verify(repositoryService, times(1)).updateEmployeeById(employeeVO);
  }

  @Test
  @DisplayName("UpdateEmployee Throws RuntimeException on Error")
  void updateEmployeeErrorTest() {
    EmployeeUpdateCmd employeeUpdateCmd = new EmployeeUpdateCmd()
        .setNif("45134320V")
        .setName("Walter")
        .setBirthYear(1998)
        .setGender("male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");
    EmployeeVO employeeVO = EmployeeVO.builder()
        .nif("45134320V")
        .name("Walter")
        .birthYear(1998)
        .gender(Gender.MALE)
        .personalPhone("+34722748406")
        .email("wmlopes0@gmail.com")
        .build();

    Mockito.when(mapper.mapToEmployeeVO(employeeUpdateCmd)).thenReturn(employeeVO);
    Mockito.when(repositoryService.updateEmployeeById(employeeVO)).thenThrow(new RuntimeException("An error occurred"));

    Assertions.assertThrows(RuntimeException.class, () -> employeeCreateImpl.updateEmployee(employeeUpdateCmd));
    Mockito.verify(mapper, times(1)).mapToEmployeeVO(employeeUpdateCmd);
    Mockito.verify(repositoryService, times(1)).updateEmployeeById(employeeVO);
  }
}