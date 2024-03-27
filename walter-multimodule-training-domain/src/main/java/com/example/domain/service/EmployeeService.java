package com.example.domain.service;

import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeUpdateVO;

public interface EmployeeService {

  List<Employee> listEmployees();

  Employee getEmployeeById(Long id);

  Employee getEmployeeByName(EmployeeNameVO employee);

  Employee addEmployee(EmployeeNameVO employee);

  Employee updateEmployeeById(EmployeeUpdateVO employeeUpdate);

  boolean deleteEmployeeById(Long id);
}
