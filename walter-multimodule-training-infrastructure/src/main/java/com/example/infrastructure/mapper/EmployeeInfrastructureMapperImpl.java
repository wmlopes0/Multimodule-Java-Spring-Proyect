package com.example.infrastructure.mapper;

import com.example.domain.entity.Employee;
import com.example.infrastructure.entity.EmployeeEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeInfrastructureMapperImpl implements EmployeeInfrastructureMapper {

  @Override
  public Employee mapToDomain(EmployeeEntity employeeEntity) {
    return new Employee()
        .setNumber(employeeEntity.getNumber())
        .setName(employeeEntity.getName());
  }

  @Override
  public EmployeeEntity mapToEntity(EmployeeNameVO employeeNameVO) {
    return new EmployeeEntity()
        .setName(employeeNameVO.getName());
  }

  @Override
  public EmployeeEntity mapToEntity(EmployeeUpdateVO employeeUpdateVO) {
    return new EmployeeEntity()
        .setNumber(employeeUpdateVO.getNumber())
        .setName(employeeUpdateVO.getName());
  }
}
