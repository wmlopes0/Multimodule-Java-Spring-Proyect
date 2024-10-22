package com.example.contract.employee.mapper;

import com.example.application.employee.cmd.dto.AddEmployeeToCompanyCmd;
import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.cmd.dto.RemoveEmployeeFromCompanyCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.domain.entity.Employee;
import org.example.rest.model.CompanyDTO;
import org.example.rest.model.EmployeeRequestDTO;
import org.example.rest.model.EmployeeResponseDTO;
import org.example.rest.model.EmployeeUpdateDTO;

public interface EmployeeContractMapper {

  EmployeeCreateCmd mapToEmployeeCreateCmd(EmployeeRequestDTO employeeRequestDTO);

  EmployeeDeleteCmd mapToEmployeeDeleteCmd(String nif);

  EmployeeUpdateCmd mapToEmployeeUpdateCmd(String nif, EmployeeUpdateDTO employeeUpdateDTO);

  EmployeeByIdQuery mapToEmployeeByIdQuery(String nif);

  EmployeeByNameQuery mapToEmployeeByNameQuery(String name);

  EmployeeResponseDTO mapToResponseDTO(Employee employee);

  AddEmployeeToCompanyCmd mapToAddEmployeeToCompanyCmd(String nif, CompanyDTO companyDTO);

  RemoveEmployeeFromCompanyCmd removeEmployeeFromCompanyCmd(String nif, CompanyDTO companyDTO);
}
