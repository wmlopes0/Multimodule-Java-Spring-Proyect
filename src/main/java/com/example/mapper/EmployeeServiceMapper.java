package com.example.mapper;

import com.example.cmd.EmployeeCreateCmd;
import com.example.cmd.EmployeeUpdateCmd;
import com.example.domain.Employee;
import com.example.entity.EmployeeEntity;

public interface EmployeeServiceMapper {

  Employee mapToDomain(EmployeeEntity employeeEntity);

  EmployeeEntity mapToEntity(EmployeeCreateCmd employeeCreateCmd);

  EmployeeEntity mapToEntity(EmployeeUpdateCmd employeeUpdateCmd);
}
