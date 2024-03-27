package com.example.mapper;

import com.example.cmd.EmployeeCreateCmd;
import com.example.cmd.EmployeeUpdateCmd;
import com.example.domain.Employee;
import com.example.dto.EmployeeNameDTO;
import com.example.dto.EmployeeNameDetailsDTO;
import com.example.dto.EmployeeResponseDTO;

public interface EmployeeControllerMapper {

  EmployeeNameDetailsDTO mapToDetailsDTO(Employee employee);

  EmployeeResponseDTO mapToResponseDTO(Employee employee);

  EmployeeCreateCmd mapToEmployeeCreateCmd(EmployeeNameDTO employeeNameDTO);

  EmployeeUpdateCmd mapToEmployeeUpdateCmd(Long id, EmployeeNameDTO employeeNameDTO);
}
