package com.example.infrastructure.mapper;

import com.example.domain.entity.Employee;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeUpdateVO;
import com.example.infrastructure.entity.EmployeeEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapperImpl implements EmployeeMapper {

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
