package com.example.mapper;

import com.example.dto.EmployeeNameDetailsDTO;
import com.example.entity.EmployeeEntity;

public interface EmployeeMapper {

  EmployeeNameDetailsDTO toDetailsDTO(EmployeeEntity employeeEntity);

}
