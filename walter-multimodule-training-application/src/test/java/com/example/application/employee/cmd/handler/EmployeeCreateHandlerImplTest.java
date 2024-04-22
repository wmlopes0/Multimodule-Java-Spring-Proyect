package com.example.application.employee.cmd.handler;

import static org.mockito.Mockito.times;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
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
class EmployeeCreateHandlerImplTest {

  @Mock
  private EmployeeService repositoryService;

  @Mock
  private EmployeeApplicationMapper mapper;

  @InjectMocks
  private EmployeeCreateHandlerImpl employeeCreateImpl;

  @Test
  @DisplayName("Add new Employee successfully")
  void addEmployee() {
    EmployeeCreateCmd employeeCreateCmd = new EmployeeCreateCmd()
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

    Mockito.when(mapper.mapToEmployeeVO(employeeCreateCmd)).thenReturn(employeeVO);
    Mockito.when(repositoryService.addEmployee(employeeVO)).thenReturn(employee);
    Employee result = employeeCreateImpl.addEmployee(employeeCreateCmd);

    Assertions.assertEquals(employee, result);
    Mockito.verify(mapper, times(1)).mapToEmployeeVO(employeeCreateCmd);
    Mockito.verify(repositoryService, times(1)).addEmployee(employeeVO);
  }

  @Test
  @DisplayName("AddEmployee Throws RuntimeException on Error")
  void addEmployeeErrorTest() {
    EmployeeCreateCmd employeeCreateCmd = new EmployeeCreateCmd()
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

    Mockito.when(mapper.mapToEmployeeVO(employeeCreateCmd)).thenReturn(employeeVO);
    Mockito.when(repositoryService.addEmployee(employeeVO)).thenThrow(new RuntimeException("An error occurred"));

    Assertions.assertThrows(RuntimeException.class, () -> employeeCreateImpl.addEmployee(employeeCreateCmd));
    Mockito.verify(mapper, times(1)).mapToEmployeeVO(employeeCreateCmd);
    Mockito.verify(repositoryService, times(1)).addEmployee(employeeVO);
  }
}