package com.example.application.employee.cmd.handler;

import static org.mockito.Mockito.times;

import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.mapper.EmployeeApplicationMapper;
import com.example.domain.entity.Employee;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.EmployeeUpdateVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Update Employee information successfully")
  void updateEmployeeTest(String name) {
    EmployeeUpdateCmd employeeUpdateCmd = new EmployeeUpdateCmd(1L, name);
    EmployeeUpdateVO employeeUpdateVO = EmployeeUpdateVO.builder().number(1L).name(name).build();
    Employee employee = new Employee().setName(name);

    Mockito.when(mapper.mapToEmployeeUpdateVO(employeeUpdateCmd)).thenReturn(employeeUpdateVO);
    Mockito.when(repositoryService.updateEmployeeById(employeeUpdateVO)).thenReturn(employee);
    Employee result = employeeCreateImpl.updateEmployee(employeeUpdateCmd);

    Assertions.assertEquals(employee, result);
    Mockito.verify(mapper, times(1)).mapToEmployeeUpdateVO(employeeUpdateCmd);
    Mockito.verify(repositoryService, times(1)).updateEmployeeById(employeeUpdateVO);
  }
}