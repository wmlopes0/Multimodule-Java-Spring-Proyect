package com.example.service;

import com.example.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> listAllEmployees();

    void addEmployee(Employee emp);

    void deleteEmployeeById(Long id);

    Employee firstEmployeeContainsName(String namePart);
}
