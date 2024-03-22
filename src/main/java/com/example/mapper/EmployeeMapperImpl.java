package com.example.mapper;

import com.example.model.Employee;
import com.example.model.EmployeeNameDetailsDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeMapperImpl implements EmployeeMapper {
    @Override
    public EmployeeNameDetailsDTO toDetailsDTO(Employee e) {
        return new EmployeeNameDetailsDTO(e.getNumber(),
                Optional.ofNullable(e.getName())
                        .map(String::toUpperCase)
                        .orElse(null),
                Optional.ofNullable(e.getName())
                        .map(String::length)
                        .orElse(0));
    }
}
