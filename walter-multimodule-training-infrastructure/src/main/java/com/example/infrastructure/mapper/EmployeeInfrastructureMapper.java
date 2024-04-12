package com.example.infrastructure.mapper;

import com.example.domain.entity.Employee;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeNifVO;
import com.example.domain.vo.EmployeeVO;
import com.example.infrastructure.entity.EmployeeEntity;

public interface EmployeeInfrastructureMapper {

  Employee mapToDomain(EmployeeEntity employeeEntity);

  EmployeeEntity mapToEntity(EmployeeNameVO employeeNameVO);

  EmployeeEntity mapToEntity(EmployeeNifVO employeeUpdateVO);

  EmployeeEntity mapToEntity(EmployeeVO employeeUpdateVO);
}
