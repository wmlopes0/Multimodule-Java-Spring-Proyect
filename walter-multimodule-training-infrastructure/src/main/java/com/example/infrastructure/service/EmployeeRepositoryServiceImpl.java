package com.example.infrastructure.service;

import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.entity.Phone;
import com.example.domain.entity.PhoneType;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeNifVO;
import com.example.domain.vo.EmployeeVO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.mapper.EmployeeInfrastructureMapper;
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
          return employeeRepository.save(existingEmployee);
        })
        .orElse(null));
  }

  @Override
  public boolean deleteEmployeeById(EmployeeNifVO employee) {
    if (employeeRepository.existsById(employee.getNif())) {
      employeeRepository.deleteById(employee.getNif());
      return true;
    }
    return false;
  }

  private void updatePhones(EmployeeEntity existingEmployee, EmployeeVO employeeVO) {
    List<Phone> updatedPhones = existingEmployee.getPhones();

    if (employeeVO.getPersonalPhone() != null) {
      Phone personalPhone = employeeInfrastructureMapper.createPhone(employeeVO.getPersonalPhone(), PhoneType.PERSONAL);
      updatedPhones.removeIf(phone -> phone.getType() == PhoneType.PERSONAL);
      updatedPhones.add(personalPhone);
    }

    if (employeeVO.getCompanyPhone() != null) {
      Phone companyPhone = employeeInfrastructureMapper.createPhone(employeeVO.getCompanyPhone(), PhoneType.COMPANY);
      updatedPhones.removeIf(phone -> phone.getType() == PhoneType.COMPANY);
      updatedPhones.add(companyPhone);
    }
  }
}