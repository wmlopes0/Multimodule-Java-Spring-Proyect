package com.example.mapper;

import com.example.model.Employee;
import com.example.model.EmployeeNameDetailsDTO;

public interface EmployeeMapper {

    EmployeeNameDetailsDTO toDetailsDTO(Employee employee);

}
