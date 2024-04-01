package com.example.infrastructure.mapper;

import com.example.domain.entity.Employee;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeUpdateVO;
import com.example.infrastructure.entity.EmployeeEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapperImpl implements EmployeeMapper {

  @Override
  public Employee mapToEmployee(EmployeeEntity employeeEntity) {
    return null;
  }

  @Override
  public EmployeeEntity mapToEmployeeEntity(EmployeeNameVO employeeNameVO) {
    return null;
  }

  @Override
  public EmployeeEntity mapToEmployeeEntity(EmployeeUpdateVO employeeUpdateVO) {
    return null;
  }

  //  @Override
  //  public EmployeeCreateCmd mapToEmployeeCreateCmd(EmployeeNameDTO employeeNameDTO) {
  //    return new EmployeeCreateCmd(employeeNameDTO.getName());
  //  }
  //
  //  @Override
  //  public EmployeeUpdateCmd mapToEmployeeUpdateCmd(Long id, EmployeeNameDTO employeeNameDTO) {
  //    return new EmployeeUpdateCmd(id, employeeNameDTO.getName());
  //  }
  //
  //  @Override
  //  public EmployeeNameDetailsDTO mapToDetailsDTO(Employee employee) {
  //    return new EmployeeNameDetailsDTO(employee.getNumber(),
  //        Optional.ofNullable(employee.getName())
  //            .map(String::toUpperCase)
  //            .orElse(null),
  //        Optional.ofNullable(employee.getName())
  //            .map(String::length)
  //            .orElse(0));
  //  }
  //
  //  @Override
  //  public EmployeeResponseDTO mapToResponseDTO(Employee employee) {
  //    return new EmployeeResponseDTO()
  //        .setNumber(employee.getNumber())
  //        .setName(employee.getName());
  //  }
}
