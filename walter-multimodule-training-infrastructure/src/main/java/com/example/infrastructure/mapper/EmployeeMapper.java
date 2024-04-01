package com.example.infrastructure.mapper;

import com.example.domain.entity.Employee;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeUpdateVO;
import com.example.infrastructure.entity.EmployeeEntity;

public interface EmployeeMapper {

  Employee mapToEmployee(EmployeeEntity employeeEntity);

  EmployeeEntity mapToEmployeeEntity(EmployeeNameVO employeeNameVO);

  EmployeeEntity mapToEmployeeEntity(EmployeeUpdateVO employeeUpdateVO);
}
