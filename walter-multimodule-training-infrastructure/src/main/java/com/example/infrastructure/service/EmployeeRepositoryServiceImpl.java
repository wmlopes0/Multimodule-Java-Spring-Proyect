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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeRepositoryServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;

  private final EmployeeInfrastructureMapper employeeInfrastructureMapper;

  @Override
  public List<Employee> listEmployees() {
    return employeeRepository.findAll().stream()
        .map(employeeInfrastructureMapper::mapToDomain)
        .toList();
  }

  @Override
  public Employee getEmployeeById(EmployeeNifVO employee) {
    return employeeRepository.findById(employee.getNif())
        .map(employeeInfrastructureMapper::mapToDomain)
        .orElse(null);
  }

  @Override
  public Employee getEmployeeByName(EmployeeNameVO employee) {
    return employeeRepository.findFirstByNameContainingIgnoreCase(employee.getName())
        .map(employeeInfrastructureMapper::mapToDomain)
        .orElse(null);
  }

  @Override
  public Employee addEmployee(EmployeeVO employee) {
    return employeeInfrastructureMapper.mapToDomain(
        employeeRepository.save(employeeInfrastructureMapper.mapToEntity(employee)));
  }

  @Override
  public Employee updateEmployeeById(EmployeeVO employeeVO) {
    return employeeInfrastructureMapper.mapToDomain(employeeRepository.findById(employeeVO.getNif())
        .map(existingEmployee -> {
          updateEmployeeDetails(existingEmployee, employeeVO);
          return employeeRepository.save(existingEmployee);
        })
        .orElseThrow(() -> new EmployeeNotFoundException("No employee found with that ID.")));
  }

  @Override
  public boolean deleteEmployeeById(EmployeeNifVO employee) {
    if (employeeRepository.existsById(employee.getNif())) {
      employeeRepository.deleteById(employee.getNif());
      return true;
    }
    return false;
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
      PhoneEntity companyPhone = employeeInfrastructureMapper.createPhone(employeeVO.getCompanyPhone(), PhoneType.COMPANY);
      updatedPhones.removeIf(phone -> phone.getType() == PhoneType.COMPANY);
      updatedPhones.add(companyPhone);
    }
    if (employeeVO.getPersonalPhone() != null) {
      PhoneEntity personalPhone = employeeInfrastructureMapper.createPhone(employeeVO.getPersonalPhone(), PhoneType.PERSONAL);
      updatedPhones.removeIf(phone -> phone.getType() == PhoneType.PERSONAL);
      updatedPhones.add(personalPhone);
    }
    existingEmployee.setPhones(updatedPhones);
  }
}