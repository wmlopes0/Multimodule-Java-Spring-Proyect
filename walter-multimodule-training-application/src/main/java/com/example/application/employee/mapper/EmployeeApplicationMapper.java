package com.example.application.employee.mapper;

import java.util.Optional;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.domain.entity.Gender;
import com.example.domain.vo.employee.EmployeeNameVO;
import com.example.domain.vo.employee.EmployeeNifVO;
import com.example.domain.vo.employee.EmployeeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface EmployeeApplicationMapper {

  @Mapping(target = "gender", expression = "java(parseGender(employeeCreateCmd.getGender()))")
  EmployeeVO mapToEmployeeVO(EmployeeCreateCmd employeeCreateCmd);

  @Mapping(target = "gender", expression = "java(parseGender(employeeUpdateCmd.getGender()))")
  EmployeeVO mapToEmployeeVO(EmployeeUpdateCmd employeeUpdateCmd);

  EmployeeNifVO mapToEmployeeNifVO(EmployeeDeleteCmd employeeDeleteCmd);

  EmployeeNifVO mapToEmployeeNifVO(EmployeeByIdQuery employeeByIdQuery);

  EmployeeNameVO mapToEmployeeNameVO(EmployeeByNameQuery employeeByNameQuery);

  default Gender parseGender(String gender) {
    return Optional.ofNullable(gender)
        .map(String::toLowerCase)
        .filter(g -> g.equals("male") || g.equals("female"))
        .map(g -> g.equals("male") ? Gender.MALE : Gender.FEMALE)
        .orElseThrow(() -> new IllegalArgumentException("Invalid gender provided."));
  }
}
