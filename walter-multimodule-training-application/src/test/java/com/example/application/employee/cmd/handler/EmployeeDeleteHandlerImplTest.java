package com.example.application.employee.cmd.handler;

import static org.mockito.Mockito.times;

import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.mapper.EmployeeApplicationMapper;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.EmployeeNifVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeDeleteHandlerImplTest {

  @Mock
  private EmployeeService repositoryService;

  @Mock
  private EmployeeApplicationMapper mapper;

  @InjectMocks
  private EmployeeDeleteHandlerImpl employeeDeleteImpl;

  @ParameterizedTest
  @CsvSource(value = {
      "true",
      "false"
  })
  @DisplayName("Delete Employee by ID successfully, and deletion fails for non-existent ID")
  void deleteEmployeeTest(boolean result) {
    EmployeeDeleteCmd employeeDeleteCmd = new EmployeeDeleteCmd("45134320V");
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder()
        .nif("45134320V")
        .build();

    Mockito.when(mapper.mapToEmployeeNifVO(employeeDeleteCmd)).thenReturn(employeeNifVO);
    Mockito.when(repositoryService.deleteEmployeeById(employeeNifVO)).thenReturn(result);

    Assertions.assertTrue(employeeDeleteImpl.deleteEmployee(employeeDeleteCmd));
    Mockito.verify(mapper, times(1)).mapToEmployeeNifVO(employeeDeleteCmd);
    Mockito.verify(repositoryService, times(1)).deleteEmployeeById(employeeNifVO);
  }

  @Test
  @DisplayName("DeleteEmployee Throws RuntimeException on Error")
  void deleteEmployeeErrorTest() {
    EmployeeDeleteCmd employeeDeleteCmd = new EmployeeDeleteCmd("45134320V");
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder()
        .nif("45134320V")
        .build();

    Mockito.when(mapper.mapToEmployeeNifVO(employeeDeleteCmd)).thenReturn(employeeNifVO);
    Mockito.when(repositoryService.deleteEmployeeById(employeeNifVO)).thenThrow(new RuntimeException("An error occurred"));

    Assertions.assertThrows(RuntimeException.class, () -> employeeDeleteImpl.deleteEmployee(employeeDeleteCmd));
    Mockito.verify(mapper, times(1)).mapToEmployeeNifVO(employeeDeleteCmd);
    Mockito.verify(repositoryService, times(1)).deleteEmployeeById(employeeNifVO);
  }
}