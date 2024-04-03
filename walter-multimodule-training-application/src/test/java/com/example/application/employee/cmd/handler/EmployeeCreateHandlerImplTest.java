package com.example.application.employee.cmd.handler;

import static org.mockito.Mockito.times;

import com.example.application.employee.cmd.cmd.EmployeeCreateCmd;
import com.example.application.employee.mapper.EmployeeApplicationMapper;
import com.example.domain.entity.Employee;
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
    EmployeeCreateCmd employeeCreateCmd = new EmployeeCreateCmd("Walter");
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();
    Employee employee = new Employee().setName("Walter");

    Mockito.when(mapper.mapToEmployeeNameVO(employeeCreateCmd)).thenReturn(employeeNameVO);
    Mockito.when(repositoryService.addEmployee(employeeNameVO)).thenReturn(employee);
    Employee result = employeeCreateImpl.addEmployee(employeeCreateCmd);

    Assertions.assertEquals(employee, result);
    Mockito.verify(mapper, times(1)).mapToEmployeeNameVO(employeeCreateCmd);
    Mockito.verify(repositoryService, times(1)).addEmployee(employeeNameVO);
  }
}