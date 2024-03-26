package com.example.service;

import com.example.model.EmployeeDTO;
import com.example.model.EmployeeNameDetailsDTO;
import com.example.model.EmployeeResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {
    ResponseEntity<List<EmployeeNameDetailsDTO>> listEmployees();

    ResponseEntity<EmployeeNameDetailsDTO> getEmployeeById(Long id);

    ResponseEntity<EmployeeNameDetailsDTO> getEmployeeByName(String name);

    ResponseEntity<EmployeeResponseDTO> addEmployee(EmployeeDTO employeeRequest);

    ResponseEntity<EmployeeResponseDTO> updateEmployeeById(Long id, EmployeeDTO employeeUpdate);

    ResponseEntity<Object> deletedEmployeeById(Long id);
}
