package com.example.application.employee.query.handler;

import com.example.application.employee.query.query.EmployeeByNameQuery;
import com.example.application.employee.mapper.EmployeeApplicationMapper;
import com.example.domain.entity.Employee;
import com.example.domain.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeGetByNameHandlerImpl implements EmployeeGetByNameHandler {

  private final EmployeeApplicationMapper mapper;

  private final EmployeeService repositoryService;

  @Override
  public Employee getEmployeeByName(EmployeeByNameQuery employeeByNameQuery) {
    return repositoryService.getEmployeeByName(
        mapper.mapToEmployeeNameVO(employeeByNameQuery));
  }
}
