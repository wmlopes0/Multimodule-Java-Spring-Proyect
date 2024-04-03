package com.example.application.employee.mapper;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeUpdateVO;
import org.springframework.stereotype.Component;

@Component
public class EmployeeApplicationMapperImpl implements EmployeeApplicationMapper {

  @Override
  public EmployeeNameVO mapToEmployeeNameVO(EmployeeCreateCmd employeeCreateCmd) {
    return EmployeeNameVO.builder()
        .name(employeeCreateCmd.getName())
        .build();
  }

  @Override
  public EmployeeNameVO mapToEmployeeNameVO(EmployeeByNameQuery employeeByNameQuery) {
    return EmployeeNameVO.builder()
        .name(employeeByNameQuery.getName())
        .build();
  }

  @Override
  public EmployeeUpdateVO mapToEmployeeUpdateVO(EmployeeUpdateCmd employeeUpdateCmd) {
    return EmployeeUpdateVO.builder()
        .number(employeeUpdateCmd.getNumber())
        .name(employeeUpdateCmd.getName())
        .build();
  }
}
