package com.example.infrastructure.service;

import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeUpdateVO;

public class EmployeeServiceImpl implements EmployeeService {

  @Override
  public List<Employee> listEmployees() {
    return null;
  }

  @Override
  public Employee getEmployeeById(Long id) {
    return null;
  }

  @Override
  public Employee getEmployeeByName(EmployeeNameVO employee) {
    return null;
  }

  @Override
  public Employee addEmployee(EmployeeNameVO employee) {
    return null;
  }

  @Override
  public Employee updateEmployeeById(EmployeeUpdateVO employeeUpdate) {
    return null;
  }

  @Override
  public boolean deleteEmployeeById(Long id) {
    return false;
  }
}
