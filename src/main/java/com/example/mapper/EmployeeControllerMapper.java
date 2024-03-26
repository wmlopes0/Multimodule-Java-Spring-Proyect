package com.example.mapper;

import com.example.domain.Employee;
import com.example.dto.EmployeeNameDetailsDTO;
import com.example.dto.EmployeeResponseDTO;

public interface EmployeeControllerMapper {

  EmployeeNameDetailsDTO mapToDetailsDTO(Employee employee);

  EmployeeResponseDTO mapToResponseDTO(Employee employee);

}
