package com.example.service;

import java.util.List;

import com.example.cmd.EmployeeCreateCmd;
import com.example.cmd.EmployeeUpdateCmd;
import com.example.domain.Employee;
import com.example.mapper.EmployeeServiceMapper;
import com.example.query.EmployeeByNameQuery;
import com.example.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;

  private final EmployeeServiceMapper serviceMapper;

  @Override
  public List<Employee> listEmployees() {
    return employeeRepository.findAll().stream()
        .map(serviceMapper::mapToDomain)
        .toList();
  }

  @Override
  public Employee getEmployeeById(Long id) {
    return employeeRepository.findById(id)
        .map(serviceMapper::mapToDomain)
        .orElse(null);
  }

  @Override
  public Employee getEmployeeByName(EmployeeByNameQuery employeeQuery) {
    return employeeRepository.findFirstByNameContainingIgnoreCase(employeeQuery.getName())
        .map(serviceMapper::mapToDomain)
        .orElse(null);
  }

  @Override
  public Employee addEmployee(EmployeeCreateCmd employeeCreateCmd) {
    return serviceMapper.mapToDomain(
        employeeRepository.save(serviceMapper.mapToEntity(employeeCreateCmd)));
  }

  @Override
  public Employee updateEmployeeById(EmployeeUpdateCmd employeeUpdateCmd) {
    if (employeeRepository.existsById(employeeUpdateCmd.getNumber())) {
      return serviceMapper.mapToDomain(
          employeeRepository.save(serviceMapper.mapToEntity(employeeUpdateCmd)));
    }
    return null;
  }

  @Override
  public boolean deleteEmployeeById(Long id) {
    if (employeeRepository.existsById(id)) {
      employeeRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
