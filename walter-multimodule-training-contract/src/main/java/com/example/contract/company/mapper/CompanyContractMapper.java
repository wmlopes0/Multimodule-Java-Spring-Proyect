package com.example.contract.company.mapper;

import java.util.ArrayList;
import java.util.List;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.application.company.cmd.dto.CompanyDeleteCmd;
import com.example.application.company.cmd.dto.CompanyUpdateCmd;
import com.example.application.company.query.dto.CompanyByIdQuery;
import com.example.domain.entity.Company;
import com.example.domain.entity.Employee;
import org.example.rest.model.CompanyRequestDTO;
import org.example.rest.model.CompanyResponseDTO;
import org.example.rest.model.CompanyUpdateDTO;
import org.example.rest.model.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface CompanyContractMapper {

  CompanyCreateCmd mapToCompanyCreateCmd(CompanyRequestDTO companyRequestDTO);

  CompanyDeleteCmd mapToCompanyDeleteCmd(String cif);

  CompanyUpdateCmd mapToCompanyUpdateCmd(String cif, CompanyUpdateDTO companyUpdateDTO);

  CompanyByIdQuery mapToCompanyByIdQuery(String cif);

  @Mapping(target = "employees", source = "employees", qualifiedByName = "mapToEmployeeDTOList")
  CompanyResponseDTO mapToCompanyResponseDTO(Company company);

  EmployeeDTO mapToEmployeeDTO(Employee employee);

  @Named("mapToEmployeeDTOList")
  default List<EmployeeDTO> mapToEmployeeDTOList(List<Employee> employees) {
    if (employees == null) {
      return new ArrayList<>();
    }
    return employees.stream()
        .map(this::mapToEmployeeDTO)
        .toList();
  }
}
