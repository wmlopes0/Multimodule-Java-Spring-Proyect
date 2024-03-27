package com.example.domain.service;

import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.vo.EmployeeVO;

public interface EmployeeService {

  List<Employee> listEmployees();

  Employee getEmployeeById(Long id);

  Employee getEmployeeByName(EmployeeVO employee);

  Employee addEmployee(EmployeeVO employee);

  Employee updateEmployeeById(EmployeeVO employeeUpdate);

  boolean deleteEmployeeById(Long id);
}
