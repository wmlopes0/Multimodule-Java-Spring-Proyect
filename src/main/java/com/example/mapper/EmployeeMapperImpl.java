package com.example.mapper;

import java.util.Optional;

import com.example.dto.EmployeeNameDetailsDTO;
import com.example.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

@Service
public class EmployeeMapperImpl implements EmployeeMapper {

  @Override
  public EmployeeNameDetailsDTO toDetailsDTO(EmployeeEntity e) {
    return new EmployeeNameDetailsDTO(e.getNumber(),
        Optional.ofNullable(e.getName())
            .map(String::toUpperCase)
            .orElse(null),
        Optional.ofNullable(e.getName())
            .map(String::length)
            .orElse(0));
  }
}
