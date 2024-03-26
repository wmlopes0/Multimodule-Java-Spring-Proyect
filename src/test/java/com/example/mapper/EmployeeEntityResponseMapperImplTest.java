package com.example.mapper;

import com.example.dto.EmployeeResponseDTO;
import com.example.entity.EmployeeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmployeeEntityResponseMapperImplTest {

  private final EmployeeResponseMapperImpl employeeResponseMapper = new EmployeeResponseMapperImpl();

  @ParameterizedTest
  @CsvSource(value = {
      "1,Walter",
      "1,null"
  }, nullValues = {"null"})
  @DisplayName("Mapping Employee to EmployeeResponseDTO correctly")
  void toResponseDTO(Long number, String name) {
    EmployeeEntity employeeEntity = new EmployeeEntity(number, name);
    EmployeeResponseDTO expected = new EmployeeResponseDTO(number, name);
    EmployeeResponseDTO result = employeeResponseMapper.toResponseDTO(employeeEntity);

    Assertions.assertEquals(expected, result);
  }
}