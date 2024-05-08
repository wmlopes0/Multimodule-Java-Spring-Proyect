package com.example.infrastructure.service;

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
import com.example.infrastructure.mapper.company.CompanyInfrastructureMapper;
import com.example.infrastructure.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyRepositoryServiceImpl implements CompanyService {

  private final CompanyRepository companyRepository;

  private final EmployeeService employeeService;

  private final CompanyInfrastructureMapper companyMapper;

  @Override
  public List<Company> getCompanies() {
    List<Company> companies = companyRepository.findAll().stream()
        .map(companyMapper::mapToDomain)
        .toList();

    companies.forEach(company -> {
      List<Employee> employees = employeeService.findEmployeesByCompanyId(company.getCif());
      company.setEmployees(employees);
    });
    return companies;
  }

  @Override
  public Company getCompany(String cif) {
    CompanyEntity companyEntity = companyRepository.findById(cif).orElse(null);
    if (companyEntity == null) {
      return null;
    }
    Company company = companyMapper.mapToDomain(companyEntity);
    List<Employee> employees = employeeService.findEmployeesByCompanyId(cif);
    company.setEmployees(employees);
    return company;
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

    CompanyEntity companyEntity = companyRepository.findById(cif).orElse(null);
    if (companyEntity == null) {
      return null;
    }

    EmployeeVO employeeUpdated = EmployeeVO.builder()
        .nif(nif)
        .company(cif)
        .build();

    employeeService.updateEmployeeById(employeeUpdated);

    Company company = companyMapper.mapToDomain(companyEntity);
    List<Employee> employees = employeeService.findEmployeesByCompanyId(cif);
    company.setEmployees(employees);
    return company;
  }

  @Override
  public boolean removeEmployeeFromCompany(String nif, String cif) {
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder().nif(nif).build();
    Employee employee = employeeService.getEmployeeById(employeeNifVO);
    if (employee == null) {
      return false;
    }

    CompanyEntity company = companyRepository.findById(cif).orElse(null);
    if (company == null) {
      return false;
    }

    EmployeeVO employeeUpdate = EmployeeVO.builder()
        .nif(nif)
        .company(null)
        .build();

    employeeService.removeCompanyFromEmployee(employeeUpdate);

    return true;
  }
}
