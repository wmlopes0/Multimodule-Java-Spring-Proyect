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
import org.springframework.stereotype.Component;

@Component
public class EmployeeApplicationMapperImpl implements EmployeeApplicationMapper {

  @Override
  public EmployeeVO mapToEmployeeVO(EmployeeCreateCmd employeeCreateCmd) {
    Gender gender = parseGender(employeeCreateCmd.getGender());
    return EmployeeVO.builder()
        .nif(employeeCreateCmd.getNif())
        .name(employeeCreateCmd.getName())
        .surname(employeeCreateCmd.getSurname())
        .birthYear(employeeCreateCmd.getBirthYear())
        .gender(gender)
        .personalPhone(employeeCreateCmd.getPersonalPhone())
        .companyPhone(employeeCreateCmd.getCompanyPhone())
        .email(employeeCreateCmd.getEmail())
        .build();
  }

  @Override
  public EmployeeVO mapToEmployeeVO(EmployeeUpdateCmd employeeUpdateCmd) {
    Gender gender = parseGender(employeeUpdateCmd.getGender());
    return EmployeeVO.builder()
        .nif(employeeUpdateCmd.getNif())
        .name(employeeUpdateCmd.getName())
        .surname(employeeUpdateCmd.getSurname())
        .birthYear(employeeUpdateCmd.getBirthYear())
        .gender(gender)
        .personalPhone(employeeUpdateCmd.getPersonalPhone())
        .companyPhone(employeeUpdateCmd.getCompanyPhone())
        .email(employeeUpdateCmd.getEmail())
        .build();
  }

  @Override
  public EmployeeNifVO mapToEmployeeNifVO(EmployeeDeleteCmd employeeDeleteCmd) {
    return EmployeeNifVO.builder()
        .nif(employeeDeleteCmd.getNif())
        .build();
  }

  @Override
  public EmployeeNifVO mapToEmployeeNifVO(EmployeeByIdQuery employeeByIdQuery) {
    return EmployeeNifVO.builder()
        .nif(employeeByIdQuery.getNif())
        .build();
  }

  @Override
  public EmployeeNameVO mapToEmployeeNameVO(EmployeeByNameQuery employeeByNameQuery) {
    return EmployeeNameVO.builder()
        .name(employeeByNameQuery.getName())
        .build();
  }

  private Gender parseGender(String gender) {
    return Optional.ofNullable(gender)
        .map(String::toLowerCase)
        .filter(g -> g.equals("male") || g.equals("female"))
        .map(g -> g.equals("male") ? Gender.MALE : Gender.FEMALE)
        .orElseThrow(() -> new IllegalArgumentException("Invalid gender provided."));
  }

}
