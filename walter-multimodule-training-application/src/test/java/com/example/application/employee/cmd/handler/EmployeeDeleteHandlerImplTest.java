package com.example.application.employee.cmd.handler;

import static org.mockito.Mockito.times;

import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.EmployeeUpdateVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeDeleteHandlerImplTest {

  @Mock
  private EmployeeService repositoryService;

  @InjectMocks
  private EmployeeDeleteHandlerImpl employeeDeleteImpl;

  @Test
  @DisplayName("Delete Employee by ID successfully")
  void deleteEmployeeTest() {
    EmployeeDeleteCmd employeeDeleteCmd = new EmployeeDeleteCmd(1L);

    Mockito.when(repositoryService.deleteEmployeeById(employeeDeleteCmd.getNumber())).thenReturn(true);

    Assertions.assertTrue(employeeDeleteImpl.deleteEmployee(employeeDeleteCmd));
    Mockito.verify(repositoryService, times(1)).deleteEmployeeById(employeeDeleteCmd.getNumber());
  }

  @Test
  @DisplayName("Employee deletion fails for non-existent ID")
  void deleteEmployeeNotFoundTest() {
    EmployeeDeleteCmd employeeDeleteCmd = new EmployeeDeleteCmd(1L);

    Mockito.when(repositoryService.deleteEmployeeById(employeeDeleteCmd.getNumber())).thenReturn(false);

    Assertions.assertFalse(employeeDeleteImpl.deleteEmployee(employeeDeleteCmd));
    Mockito.verify(repositoryService, times(1)).deleteEmployeeById(employeeDeleteCmd.getNumber());
  }

  @Test
  @DisplayName("DeleteEmployee Throws RuntimeException on Error")
  void deleteEmployeeErrorTest() {
    EmployeeDeleteCmd employeeDeleteCmd = new EmployeeDeleteCmd(1L);

    Mockito.when(repositoryService.deleteEmployeeById(employeeDeleteCmd.getNumber())).thenThrow(new RuntimeException("An error occurred"));

    Assertions.assertThrows(RuntimeException.class, () -> employeeDeleteImpl.deleteEmployee(employeeDeleteCmd));
    Mockito.verify(repositoryService, times(1)).deleteEmployeeById(employeeDeleteCmd.getNumber());
  }
}