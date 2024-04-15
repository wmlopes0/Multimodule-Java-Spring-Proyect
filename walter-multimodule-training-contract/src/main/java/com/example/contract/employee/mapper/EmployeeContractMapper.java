package com.example.contract.employee.mapper;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.contract.employee.dto.EmployeeNameDTO;
import com.example.contract.employee.dto.EmployeeResponseDTO;
import com.example.domain.entity.Employee;

public interface EmployeeContractMapper {

  EmployeeCreateCmd mapToEmployeeCreateCmd(EmployeeNameDTO employeeNameDTO);

  EmployeeDeleteCmd mapToEmployeeDeleteCmd(Long id);

  EmployeeUpdateCmd mapToEmployeeUpdateCmd(Long id, EmployeeNameDTO employeeNameDTO);

  EmployeeByIdQuery mapToEmployeeByIdQuery(Long id);

  EmployeeByNameQuery mapToEmployeeByNameQuery(String name);

  EmployeeNameDetailsDTO mapToDetailsDTO(Employee employee);

  EmployeeResponseDTO mapToResponseDTO(Employee employee);

}
