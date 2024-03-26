package com.example.mapper;

import com.example.domain.Employee;
import com.example.dto.EmployeeNameDetailsDTO;
import com.example.dto.EmployeeResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmployeeControllerMapperImplTest {

  private final EmployeeControllerMapperImpl employeeControllerMapper = new EmployeeControllerMapperImpl();

  @ParameterizedTest
  @CsvSource(value = {
      "Walter,WALTER,6",
      "null,null,0"
  }, nullValues = {"null"})
  @DisplayName("Mapping Employee to EmployeeNameDetailsDTO correctly")
  void toDetailsDTO(String name, String expectedName, int expectedLength) {
    Employee employee = new Employee(1L, name);
    EmployeeNameDetailsDTO expected = new EmployeeNameDetailsDTO(1L, expectedName, expectedLength);

    EmployeeNameDetailsDTO result = employeeControllerMapper.mapToDetailsDTO(employee);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "1,Walter",
      "1,null"
  }, nullValues = {"null"})
  @DisplayName("Mapping Employee to EmployeeResponseDTO correctly")
  void toResponseDTO(Long number, String name) {
    Employee employee = new Employee(1L, name);
    EmployeeResponseDTO expected = new EmployeeResponseDTO(number, name);
    EmployeeResponseDTO result = employeeControllerMapper.mapToResponseDTO(employee);

    Assertions.assertEquals(expected, result);
  }
}