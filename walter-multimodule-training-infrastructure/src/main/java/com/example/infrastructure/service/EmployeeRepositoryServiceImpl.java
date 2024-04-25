package com.example.infrastructure.service;

import java.util.ArrayList;
import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.entity.PhoneType;
import com.example.domain.exception.EmployeeNotFoundException;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.employee.EmployeeNameVO;
import com.example.domain.vo.employee.EmployeeNifVO;
import com.example.domain.vo.employee.EmployeeVO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.entity.PhoneEntity;
import com.example.infrastructure.mapper.employee.EmployeeInfrastructureMapper;
import com.example.infrastructure.repository.EmployeeRepository;
import com.jayway.jsonpath.Criteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeRepositoryServiceImpl implements EmployeeService {

  private final EmployeeRepository repository;

  private final EmployeeInfrastructureMapper mapper;

  private final MongoTemplate mongoTemplate;

  @Override
  public List<Employee> listEmployees() {
    return repository.findAll().stream()
        .map(mapper::mapToDomain)
        .toList();
  }

  @Override
  public Employee getEmployeeById(EmployeeNifVO employee) {
    return repository.findById(employee.getNif())
        .map(mapper::mapToDomain)
        .orElse(null);
  }

  @Override
  public Employee getEmployeeByName(EmployeeNameVO employee) {
    return repository.findFirstByNameContainingIgnoreCase(employee.getName())
        .map(mapper::mapToDomain)
        .orElse(null);
  }

  @Override
  public Employee addEmployee(EmployeeVO employee) {
    return mapper.mapToDomain(
        repository.save(mapper.mapToEntity(employee)));
  }

  @Override
  public Employee updateEmployeeById(EmployeeVO employeeVO) {
    return mapper.mapToDomain(repository.findById(employeeVO.getNif())
        .map(existingEmployee -> {
          updateEmployeeDetails(existingEmployee, employeeVO);
          return repository.save(existingEmployee);
        })
        .orElseThrow(() -> new EmployeeNotFoundException("No employee found with that ID.")));
  }

  @Override
  public boolean deleteEmployeeById(EmployeeNifVO employee) {
    if (repository.existsById(employee.getNif())) {
      repository.deleteById(employee.getNif());
      return true;
    }
    return false;
  }

  @Override
  @Transactional
  public void dissociateEmployeesFromCompany(String companyId) {
    CriteriaDefinition criteriaDefinition = (CriteriaDefinition) Criteria.where("company").is(companyId);
    Query query = new Query(criteriaDefinition);
    Update update = new Update().set("company", null);
    mongoTemplate.updateMulti(query, update, EmployeeEntity.class);
  }

  private void updateEmployeeDetails(EmployeeEntity existingEmployee, EmployeeVO employeeVO) {
    if (employeeVO.getName() != null) {
      existingEmployee.setName(employeeVO.getName());
    }
    if (employeeVO.getSurname() != null) {
      existingEmployee.setLastName(employeeVO.getSurname());
    }
    if (employeeVO.getBirthYear() != 0) {
      existingEmployee.setBirthYear(employeeVO.getBirthYear());
    }
    if (employeeVO.getGender() != null) {
      existingEmployee.setGender(employeeVO.getGender().getCode());
    }
    updatePhones(existingEmployee, employeeVO);
    if (employeeVO.getEmail() != null) {
      existingEmployee.setEmail(employeeVO.getEmail());
    }
  }

  private void updatePhones(EmployeeEntity existingEmployee, EmployeeVO employeeVO) {
    List<PhoneEntity> updatedPhones = new ArrayList<>(existingEmployee.getPhones());
    if (employeeVO.getCompanyPhone() != null) {
      PhoneEntity companyPhone = mapper.createPhone(employeeVO.getCompanyPhone(), PhoneType.COMPANY);
      updatedPhones.removeIf(phone -> phone.getType() == PhoneType.COMPANY);
      updatedPhones.add(companyPhone);
    }
    if (employeeVO.getPersonalPhone() != null) {
      PhoneEntity personalPhone = mapper.createPhone(employeeVO.getPersonalPhone(), PhoneType.PERSONAL);
      updatedPhones.removeIf(phone -> phone.getType() == PhoneType.PERSONAL);
      updatedPhones.add(personalPhone);
    }
    existingEmployee.setPhones(updatedPhones);
  }
}