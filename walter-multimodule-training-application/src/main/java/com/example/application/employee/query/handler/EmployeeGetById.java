package com.example.application.employee.query.handler;

import com.example.application.employee.query.EmployeeByIdQuery;
import com.example.domain.entity.Employee;

public interface EmployeeGetById {

  Employee getEmployeeById(EmployeeByIdQuery employeeByIdQuery);

}
