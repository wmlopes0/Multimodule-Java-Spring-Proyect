package com.example.mapper;

import com.example.cmd.EmployeeCreateCmd;
import com.example.cmd.EmployeeUpdateCmd;
import com.example.domain.Employee;
import com.example.entity.EmployeeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmployeeServiceMapperImplTest {

  private final EmployeeServiceMapperImpl employeeServiceMapper = new EmployeeServiceMapperImpl();

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Mapping EmployeeEntity to Employee correctly")
  void mapToDomainTest(String name) {
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, name);
    Employee expected = new Employee(1L, name);
    Employee result = employeeServiceMapper.mapToDomain(employeeEntity);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Mapping EmployeeCreateCmd to Employee correctly")
  void mapToEntityCreatedTest(String name) {
    EmployeeCreateCmd employeeCreateCmd = new EmployeeCreateCmd(name);
    EmployeeEntity expected = new EmployeeEntity().setName(name);
    EmployeeEntity result = employeeServiceMapper.mapToEntity(employeeCreateCmd);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "1,Walter",
      "1,null"
  }, nullValues = {"null"})
  @DisplayName("Mapping EmployeeUpdateCmd to Employee correctly")
  void mapToEntityUpdatedTest(Long id, String name) {
    EmployeeUpdateCmd employeeUpdateCmd = new EmployeeUpdateCmd(id, name);
    EmployeeEntity expected = new EmployeeEntity(id, name);
    EmployeeEntity result = employeeServiceMapper.mapToEntity(employeeUpdateCmd);
    Assertions.assertEquals(expected, result);
  }
}