package com.example.mapper;

import com.example.dto.EmployeeResponseDTO;
import com.example.entity.EmployeeEntity;

public interface EmployeeResponseMapper {

  EmployeeResponseDTO toResponseDTO(EmployeeEntity employeeEntity);
}
