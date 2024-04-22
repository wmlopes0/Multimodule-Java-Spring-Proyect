package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.mapper.EmployeeApplicationMapper;
import com.example.domain.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeDeleteHandlerImpl implements EmployeeDeleteHandler {

  private final EmployeeApplicationMapper mapper;

  private final EmployeeService repositoryService;

  @Override
  public boolean deleteEmployee(EmployeeDeleteCmd employeeDeleteCmd) {
    return repositoryService.deleteEmployeeById(
        mapper.mapToEmployeeNifVO(employeeDeleteCmd));
  }
}
