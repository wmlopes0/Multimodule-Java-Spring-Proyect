package com.example.application.employee.mapper;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.domain.vo.employee.EmployeeNameVO;
import com.example.domain.vo.employee.EmployeeNifVO;
import com.example.domain.vo.employee.EmployeeVO;

public interface EmployeeApplicationMapper {

  EmployeeVO mapToEmployeeVO(EmployeeCreateCmd employeeCreateCmd);

  EmployeeVO mapToEmployeeVO(EmployeeUpdateCmd employeeUpdateCmd);

  EmployeeNifVO mapToEmployeeNifVO(EmployeeDeleteCmd employeeDeleteCmd);

  EmployeeNifVO mapToEmployeeNifVO(EmployeeByIdQuery employeeByIdQuery);

  EmployeeNameVO mapToEmployeeNameVO(EmployeeByNameQuery employeeByNameQuery);

}
