package com.example.service;

import java.util.List;

import com.example.cmd.EmployeeCreateCmd;
import com.example.cmd.EmployeeUpdateCmd;
import com.example.domain.Employee;
import com.example.query.EmployeeByNameQuery;

public interface EmployeeService {

  List<Employee> listEmployees();

  Employee getEmployeeById(Long id);

  Employee getEmployeeByName(EmployeeByNameQuery employeeByNameQuery);

  Employee addEmployee(EmployeeCreateCmd employeeCreateCmd);

  Employee updateEmployeeById(EmployeeUpdateCmd employeeUpdateCmd);

  boolean deleteEmployeeById(Long id);
}
