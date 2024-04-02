package com.example.application.employee.query.handler;

import com.example.application.employee.query.EmployeeByNameQuery;
import com.example.domain.entity.Employee;

public interface EmployeeGetByName {

  Employee getEmployeeByName(EmployeeByNameQuery employeeByNameQuery);
}
