package com.example.infrastructure.service;

import java.util.ArrayList;
import java.util.List;

import com.example.domain.entity.Company;
import com.example.domain.entity.Employee;
import com.example.domain.exception.CompanyNotFoundException;
import com.example.domain.service.CompanyService;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import com.example.domain.vo.employee.EmployeeNifVO;
import com.example.domain.vo.employee.EmployeeVO;
import com.example.infrastructure.entity.CompanyEntity;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.mapper.company.CompanyInfrastructureMapper;
import com.example.infrastructure.mapper.employee.EmployeeInfrastructureMapper;
import com.example.infrastructure.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyRepositoryServiceImpl implements CompanyService {

  private final CompanyRepository companyRepository;

  private final EmployeeService employeeService;

  private final EmployeeInfrastructureMapper employeeMapper;

  private final CompanyInfrastructureMapper companyMapper;

  @Override
  public List<Company> getCompanies() {
    return companyRepository.findAll().stream()
        .map(companyMapper::mapToDomain)
        .toList();
  }

  @Override
  public Company getCompany(String cif) {
    return companyRepository.findById(cif)
        .map(companyMapper::mapToDomain)
        .orElse(null);
  }

  @Override
  public Company createCompany(CompanyCreateVO companyCreateVO) {
    return companyMapper.mapToDomain(
        companyRepository.save(companyMapper.mapToEntity(companyCreateVO)));
  }

  @Override
  public Company updateCompany(CompanyUpdateVO companyUpdateVO) {
    return companyMapper.mapToDomain(companyRepository.findById(companyUpdateVO.getCif())
        .map(existingCompany -> {
          if (existingCompany.getName() != null) {
            existingCompany.setName(companyUpdateVO.getName());
          }
          return companyRepository.save(existingCompany);
        }).orElseThrow(() -> new CompanyNotFoundException("No company found with that ID."))
    );
  }

  @Override
  public boolean deleteCompany(String cif) {
    if (companyRepository.existsById(cif)) {
      companyRepository.deleteById(cif);
      employeeService.dissociateEmployeesFromCompany(cif);
      return true;
    }
    return false;
  }

  @Override
  @Transactional
  public Company addEmployeeToCompany(String nif, String cif) {
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder().nif(nif).build();
    Employee employee = employeeService.getEmployeeById(employeeNifVO);
    if (employee == null) {
      return null;
    }

    CompanyEntity company = companyRepository.findById(cif).orElse(null);
    if (company == null) {
      return null;
    }

    EmployeeVO employeeUpdated = EmployeeVO.builder()
        .nif(nif)
        .company(cif)
        .build();

    EmployeeEntity employeeEntity = employeeMapper.mapDomainToEntity(employeeService.updateEmployeeById(employeeUpdated));

    List<EmployeeEntity> employees = company.getEmployees();
    if (employees == null) {
      employees = new ArrayList<>();
    }

    employees.add(employeeEntity);
    company.setEmployees(employees);
    companyRepository.save(company);
    return companyMapper.mapToDomain(company);

  }

  @Override
  public Company removeEmployeeFromCompany(String nif, String cif) {
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder().nif(nif).build();
    EmployeeEntity employee = employeeMapper.mapDomainToEntity(employeeService.getEmployeeById(employeeNifVO));
    if (employee == null) {
      return null;
    }

    CompanyEntity company = companyRepository.findById(cif).orElse(null);
    if (company == null) {
      return null;
    }

    EmployeeVO employeeUpdatedVO = EmployeeVO.builder()
        .nif(nif)
        .company(null)
        .build();
    employeeService.updateEmployeeById(employeeUpdatedVO);

    List<EmployeeEntity> employees = company.getEmployees();
    if (employees != null) {
      employees.removeIf(e -> e.getNif().equals(nif));
      company.setEmployees(employees);
      companyRepository.save(company);
    }

    return companyMapper.mapToDomain(company);
  }
}
