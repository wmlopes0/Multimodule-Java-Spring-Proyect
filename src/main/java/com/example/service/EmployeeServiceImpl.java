package com.example.service;

import java.util.List;

import com.example.cmd.EmployeeCreateCmd;
import com.example.cmd.EmployeeUpdateCmd;
import com.example.domain.Employee;
import com.example.query.EmployeeByNameQuery;
import com.example.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;

  @Override
  public List<Employee> listEmployees() {
    return null;
  }

  @Override
  public Employee getEmployeeById(Long id) {
    return null;
  }

  @Override
  public Employee getEmployeeByName(EmployeeByNameQuery employeeByNameQuery) {
    return null;
  }

  @Override
  public Employee addEmployee(EmployeeCreateCmd employeeCreateCmd) {
    return null;
  }

  @Override
  public Employee updateEmployeeById(EmployeeUpdateCmd employeeUpdateCmd) {
    return null;
  }

  @Override
  public boolean deletedEmployeeById(Long id) {
    return false;
  }
}
