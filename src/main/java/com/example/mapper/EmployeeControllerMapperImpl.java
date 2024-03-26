package com.example.mapper;

import java.util.Optional;

import com.example.domain.Employee;
import com.example.dto.EmployeeNameDetailsDTO;
import com.example.dto.EmployeeResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EmployeeControllerMapperImpl implements EmployeeControllerMapper {

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
