package com.example.mapper;

import java.util.Optional;

import com.example.cmd.EmployeeCreateCmd;
import com.example.cmd.EmployeeUpdateCmd;
import com.example.domain.Employee;
import com.example.dto.EmployeeNameDTO;
import com.example.dto.EmployeeNameDetailsDTO;
import com.example.dto.EmployeeResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EmployeeControllerMapperImpl implements EmployeeControllerMapper {

  @Override
  public EmployeeCreateCmd mapToEmployeeCreateCmd(EmployeeNameDTO employeeNameDTO) {
    return new EmployeeCreateCmd(employeeNameDTO.getName());
  }

  @Override
  public EmployeeUpdateCmd mapToEmployeeUpdateCmd(Long id, EmployeeNameDTO employeeNameDTO) {
    return new EmployeeUpdateCmd(id, employeeNameDTO.getName());
  }

  @Override
  public EmployeeNameDetailsDTO mapToDetailsDTO(Employee employee) {
    return new EmployeeNameDetailsDTO(employee.getNumber(),
        Optional.ofNullable(employee.getName())
            .map(String::toUpperCase)
            .orElse(null),
        Optional.ofNullable(employee.getName())
            .map(String::length)
            .orElse(0));
  }

  @Override
  public EmployeeResponseDTO mapToResponseDTO(Employee employee) {
    return new EmployeeResponseDTO()
        .setNumber(employee.getNumber())
        .setName(employee.getName());
  }
}
