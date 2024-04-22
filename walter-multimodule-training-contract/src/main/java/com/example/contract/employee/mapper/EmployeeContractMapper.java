package com.example.contract.employee.mapper;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.contract.employee.dto.employee.EmployeeRequestDTO;
import com.example.contract.employee.dto.employee.EmployeeResponseDTO;
import com.example.contract.employee.dto.employee.EmployeeUpdateDTO;
import com.example.domain.entity.Employee;

public interface EmployeeContractMapper {

  EmployeeCreateCmd mapToEmployeeCreateCmd(EmployeeRequestDTO employeeRequestDTO);

  EmployeeDeleteCmd mapToEmployeeDeleteCmd(String nif);

  EmployeeUpdateCmd mapToEmployeeUpdateCmd(String nif, EmployeeUpdateDTO employeeUpdateDTO);

  EmployeeByIdQuery mapToEmployeeByIdQuery(String nif);

  EmployeeByNameQuery mapToEmployeeByNameQuery(String name);

  EmployeeResponseDTO mapToResponseDTO(Employee employee);

}
