package com.example.application.employee.query.handler;

import com.example.application.employee.query.query.EmployeeByNameQuery;
import com.example.domain.entity.Employee;

public interface EmployeeGetByNameHandler {

  Employee getEmployeeByName(EmployeeByNameQuery employeeByNameQuery);
}
