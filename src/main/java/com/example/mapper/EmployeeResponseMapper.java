package com.example.mapper;

import com.example.model.Employee;
import com.example.model.EmployeeResponseDTO;

public interface EmployeeResponseMapper {

    EmployeeResponseDTO toResponseDTO (Employee employee);
}
