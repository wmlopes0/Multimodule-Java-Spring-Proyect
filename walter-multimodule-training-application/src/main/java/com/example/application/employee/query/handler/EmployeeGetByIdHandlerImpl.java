package com.example.application.employee.query.handler;

import com.example.application.employee.mapper.EmployeeApplicationMapper;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.domain.entity.Employee;
import com.example.domain.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeGetByIdHandlerImpl implements EmployeeGetByIdHandler {

  private final EmployeeApplicationMapper mapper;

  private final EmployeeService repositoryService;

  @Override
  public Employee getEmployeeById(EmployeeByIdQuery employeeByIdQuery) {
    return repositoryService.getEmployeeById(
        mapper.mapToEmployeeNifVO(employeeByIdQuery));
  }
}
