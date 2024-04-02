package com.example.application.employee.cmd.handler;

import com.example.application.employee.cmd.EmployeeDeleteCmd;
import com.example.domain.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeDeleteImpl implements EmployeeDelete {

  private final EmployeeService repositoryService;

  @Override
  public boolean deleteEmployee(EmployeeDeleteCmd employeeDeleteCmd) {
    return repositoryService.deleteEmployeeById(employeeDeleteCmd.getNumber());
  }
}
