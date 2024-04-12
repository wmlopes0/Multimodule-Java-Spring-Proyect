package com.example.domain.service;

import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeNifVO;
import com.example.domain.vo.EmployeeVO;

public interface EmployeeService {

  List<Employee> listEmployees();

  Employee getEmployeeById(EmployeeNifVO employeeNifVO);

  Employee getEmployeeByName(EmployeeNameVO employeeNameVO);

  Employee addEmployee(EmployeeVO employeeVO);

  Employee updateEmployeeById(EmployeeVO employeeVO);

  boolean deleteEmployeeById(EmployeeNifVO employeeNifVO);
}
