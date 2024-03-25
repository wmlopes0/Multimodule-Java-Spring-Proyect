package com.example.mapper;

import com.example.model.Employee;
import com.example.model.EmployeeResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class EmployeeResponseMapperImpl implements EmployeeResponseMapper{
    @Override
    public EmployeeResponseDTO toResponseDTO(Employee employee) {
        return new EmployeeResponseDTO()
                .setNumber(employee.getNumber())
                .setName(employee.getName());
    }
}
