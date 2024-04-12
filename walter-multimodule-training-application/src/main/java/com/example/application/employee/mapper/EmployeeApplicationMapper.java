package com.example.application.employee.mapper;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByNameQuery;

public interface EmployeeApplicationMapper {

  EmployeeNameVO mapToEmployeeNameVO(EmployeeCreateCmd employeeCreateCmd);

  EmployeeNameVO mapToEmployeeNameVO(EmployeeByNameQuery employeeByNameQuery);

  EmployeeUpdateVO mapToEmployeeUpdateVO(EmployeeUpdateCmd employeeUpdateCmd);

}
