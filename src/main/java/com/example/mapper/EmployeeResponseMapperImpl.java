package com.example.mapper;

import com.example.dto.EmployeeResponseDTO;
import com.example.entity.EmployeeEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeResponseMapperImpl implements EmployeeResponseMapper {

  @Override
  public EmployeeResponseDTO toResponseDTO(EmployeeEntity employeeEntity) {
    return new EmployeeResponseDTO()
        .setNumber(employeeEntity.getNumber())
        .setName(employeeEntity.getName());
  }
}
