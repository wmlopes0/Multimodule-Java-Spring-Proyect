package com.example.infrastructure.service;

import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeUpdateVO;
import com.example.infrastructure.mapper.EmployeeMapper;
import com.example.infrastructure.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;

  private final EmployeeMapper employeeMapper;

  @Override
  public List<Employee> listEmployees() {
    return employeeRepository.findAll().stream()
        .map(employeeMapper::mapToDomain)
        .toList();
  }

  @Override
  public Employee getEmployeeById(Long id) {
    return employeeRepository.findById(id)
        .map(employeeMapper::mapToDomain)
        .orElse(null);
  }

  @Override
  public Employee getEmployeeByName(EmployeeNameVO employee) {
    return employeeRepository.findFirstByNameContainingIgnoreCase(employee.getName())
        .map(employeeMapper::mapToDomain)
        .orElse(null);
  }

  @Override
  public Employee addEmployee(EmployeeNameVO employee) {
    return employeeMapper.mapToDomain(
        employeeRepository.save(employeeMapper.mapToEntity(employee)));
  }

  @Override
  public Employee updateEmployeeById(EmployeeUpdateVO employee) {
    if (employeeRepository.existsById(employee.getNumber())) {
      return employeeMapper.mapToDomain(
          employeeRepository.save(employeeMapper.mapToEntity(employee)));
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
