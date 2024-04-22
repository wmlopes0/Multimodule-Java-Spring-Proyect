package com.example.application.employee.query.handler;

import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeListHandlerImpl implements EmployeeListHandler {

  private final EmployeeService repositoryService;

  @Override
  public List<Employee> listEmployees() {
    return repositoryService.listEmployees();
  }
}
