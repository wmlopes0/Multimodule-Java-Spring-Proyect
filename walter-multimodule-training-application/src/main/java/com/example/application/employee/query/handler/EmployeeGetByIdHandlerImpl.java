package com.example.application.employee.query.handler;

import com.example.application.employee.query.query.EmployeeByIdQuery;
import com.example.domain.entity.Employee;
import com.example.domain.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeGetByIdHandlerImpl implements EmployeeGetByIdHandler {

  private final EmployeeService repositoryService;

  @Override
  public Employee getEmployeeById(EmployeeByIdQuery employeeByIdQuery) {
    return repositoryService.getEmployeeById(employeeByIdQuery.getNumber());
  }
}
