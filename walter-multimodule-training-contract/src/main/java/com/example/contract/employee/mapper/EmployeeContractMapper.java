package com.example.contract.employee.mapper;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.contract.employee.dto.EmployeeNameDTO;
import com.example.contract.employee.dto.EmployeeNifDTO;
import com.example.contract.employee.dto.EmployeeRequestDTO;
import com.example.contract.employee.dto.EmployeeResponseDTO;
import com.example.contract.employee.dto.EmployeeUpdateDTO;
import com.example.domain.entity.Employee;

public interface EmployeeContractMapper {

  EmployeeCreateCmd mapToEmployeeCreateCmd(EmployeeRequestDTO employeeRequestDTO);

  EmployeeDeleteCmd mapToEmployeeDeleteCmd(EmployeeNifDTO employeeNifDTO);

  EmployeeUpdateCmd mapToEmployeeUpdateCmd(EmployeeUpdateDTO employeeUpdateDTO);

  EmployeeByIdQuery mapToEmployeeByIdQuery(EmployeeNifDTO employeeNifDTO);

  EmployeeByNameQuery mapToEmployeeByNameQuery(EmployeeNameDTO employeeNameDTO);

  EmployeeResponseDTO mapToResponseDTO(Employee employee);

}
