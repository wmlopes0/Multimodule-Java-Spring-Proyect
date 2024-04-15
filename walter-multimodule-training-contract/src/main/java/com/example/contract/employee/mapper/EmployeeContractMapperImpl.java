package com.example.contract.employee.mapper;

import java.util.Optional;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.contract.employee.dto.EmployeeNameDTO;
import com.example.contract.employee.dto.EmployeeResponseDTO;
import com.example.domain.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeContractMapperImpl implements EmployeeContractMapper {

  @Override
  public EmployeeCreateCmd mapToEmployeeCreateCmd(EmployeeNameDTO employeeNameDTO) {
    return new EmployeeCreateCmd(employeeNameDTO.getName());
  }

  @Override
  public EmployeeDeleteCmd mapToEmployeeDeleteCmd(Long id) {
    return new EmployeeDeleteCmd(id);
  }

  @Override
  public EmployeeUpdateCmd mapToEmployeeUpdateCmd(Long id, EmployeeNameDTO employeeNameDTO) {
    return new EmployeeUpdateCmd(id, employeeNameDTO.getName());
  }

  @Override
  public EmployeeByIdQuery mapToEmployeeByIdQuery(Long id) {
    return new EmployeeByIdQuery(id);
  }

  @Override
  public EmployeeByNameQuery mapToEmployeeByNameQuery(String name) {
    return new EmployeeByNameQuery(name);
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