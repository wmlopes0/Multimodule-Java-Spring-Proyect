package com.example.infrastructure.service;

import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeNifVO;
import com.example.domain.vo.EmployeeVO;
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
  public Employee updateEmployeeById(EmployeeVO employee) {
    if (employeeRepository.existsById(employee.getNif())) {
      return employeeInfrastructureMapper.mapToDomain(
          employeeRepository.save(employeeInfrastructureMapper.mapToEntity(employee)));
    }
    return null;
  }

  @Override
  public boolean deleteEmployeeById(EmployeeNifVO employee) {
    if (employeeRepository.existsById(employee.getNif())) {
      employeeRepository.deleteById(employee.getNif());
      return true;
    }
    return false;
  }
}
