package com.example.mapper;

import com.example.cmd.EmployeeCreateCmd;
import com.example.cmd.EmployeeUpdateCmd;
import com.example.domain.Employee;
import com.example.entity.EmployeeEntity;

public class EmployeeServiceMapperImpl implements EmployeeServiceMapper {

  @Override
  public Employee mapToDomain(EmployeeEntity employeeEntity) {
    return new Employee()
        .setNumber(employeeEntity.getNumber())
        .setName(employeeEntity.getName());
  }

  @Override
  public EmployeeEntity mapToEntity(EmployeeCreateCmd employeeCreateCmd) {
    return new EmployeeEntity()
        .setName(employeeCreateCmd.getName());
  }

  @Override
  public EmployeeEntity mapToEntity(EmployeeUpdateCmd employeeUpdateCmd) {
    return new EmployeeEntity()
        .setNumber(employeeUpdateCmd.getNumber())
        .setName(employeeUpdateCmd.getName());
  }
}
