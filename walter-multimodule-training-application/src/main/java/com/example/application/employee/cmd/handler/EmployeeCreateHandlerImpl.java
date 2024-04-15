package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.mapper.EmployeeApplicationMapper;
import com.example.domain.entity.Employee;
import com.example.domain.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeCreateHandlerImpl implements EmployeeCreateHandler {

  private final EmployeeApplicationMapper mapper;

  private final EmployeeService repositoryService;

  @Override
  public Employee addEmployee(EmployeeCreateCmd employeeCreateCmd) {
    return repositoryService.addEmployee(
        mapper.mapToEmployeeVO(employeeCreateCmd)
    );
  }
}
