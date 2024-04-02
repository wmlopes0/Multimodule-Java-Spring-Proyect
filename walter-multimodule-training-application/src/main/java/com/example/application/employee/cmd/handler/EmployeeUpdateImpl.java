package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.EmployeeUpdateCmd;
import com.example.application.employee.mapper.EmployeeApplicationMapper;
import com.example.domain.entity.Employee;
import com.example.domain.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeUpdateImpl implements EmployeeUpdate {

  private final EmployeeApplicationMapper mapper;

  private final EmployeeService repositoryService;

  @Override
  public Employee updateEmployee(EmployeeUpdateCmd employeeUpdateCmd) {
    return repositoryService.updateEmployeeById(
        mapper.mapToEmployeeUpdateVO(employeeUpdateCmd)
    );
  }
}
