package com.example.infrastructure.mapper;

import com.example.domain.entity.Employee;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeUpdateVO;
import com.example.infrastructure.entity.EmployeeEntity;

public interface EmployeeInfrastructureMapper {

  Employee mapToDomain(EmployeeEntity employeeEntity);

  EmployeeEntity mapToEntity(EmployeeNameVO employeeNameVO);

  EmployeeEntity mapToEntity(EmployeeUpdateVO employeeUpdateVO);
}
