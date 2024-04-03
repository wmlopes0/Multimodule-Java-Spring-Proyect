package com.example.application.employee.mapper;

import com.example.application.employee.cmd.cmd.EmployeeCreateCmd;
import com.example.application.employee.cmd.cmd.EmployeeUpdateCmd;
import com.example.application.employee.query.query.EmployeeByNameQuery;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeUpdateVO;

public interface EmployeeApplicationMapper {

  EmployeeNameVO mapToEmployeeNameVO(EmployeeCreateCmd employeeCreateCmd);

  EmployeeNameVO mapToEmployeeNameVO(EmployeeByNameQuery employeeByNameQuery);

  EmployeeUpdateVO mapToEmployeeUpdateVO(EmployeeUpdateCmd employeeUpdateCmd);

}
