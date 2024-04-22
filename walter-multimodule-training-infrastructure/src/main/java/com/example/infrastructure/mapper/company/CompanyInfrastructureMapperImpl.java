package com.example.infrastructure.mapper.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.domain.entity.Company;
import com.example.domain.entity.Employee;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import com.example.infrastructure.entity.CompanyEntity;
import com.example.infrastructure.mapper.employee.EmployeeInfrastructureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CompanyInfrastructureMapperImpl implements CompanyInfrastructureMapper {

  private final EmployeeInfrastructureMapper employeeMapper;

  @Override
  public Company mapToDomain(CompanyEntity companyEntity) {
    Company company = new Company()
        .setCif(companyEntity.getCif())
        .setName(companyEntity.getName());

    if (companyEntity.getEmployees() != null && !companyEntity.getEmployees().isEmpty()) {
      List<Employee> employees = companyEntity.getEmployees().stream()
          .map(employeeMapper::mapToDomain)
          .collect(Collectors.toList());
      company.setEmployees(employees);
    } else {
      company.setEmployees(new ArrayList<>());
    }
    return company;
  }

  @Override
  public CompanyEntity mapToEntity(CompanyCreateVO companyCreateVO) {
    return new CompanyEntity()
        .setCif(companyCreateVO.getCif())
        .setName(companyCreateVO.getName())
        .setEmployees(
            companyCreateVO.getEmployees().stream()
                .map(employeeMapper::mapDomainToEntity)
                .collect(Collectors.toList()));
  }

  @Override
  public CompanyEntity mapToEntity(CompanyUpdateVO companyUpdateVO) {
    return new CompanyEntity()
        .setCif(companyUpdateVO.getCif())
        .setName(companyUpdateVO.getName())
        .setEmployees(
            companyUpdateVO.getEmployees().stream()
                .map(employeeMapper::mapDomainToEntity)
                .collect(Collectors.toList()));
  }
}
