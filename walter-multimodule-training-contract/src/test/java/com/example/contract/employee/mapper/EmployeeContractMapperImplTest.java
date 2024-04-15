package com.example.contract.employee.mapper;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.contract.employee.dto.EmployeeNameDTO;
import com.example.contract.employee.dto.EmployeeResponseDTO;
import com.example.domain.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmployeeContractMapperImplTest {

  private final EmployeeContractMapper mapper = new EmployeeContractMapperImpl();

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Mapping EmployeeNameDTO to EmployeeCreateCmd correctly")
  void mapToEmployeeCreateCmdTest(String name) {
    EmployeeNameDTO employeeNameDTO = new EmployeeNameDTO(name);
    EmployeeCreateCmd expected = new EmployeeCreateCmd(name);
    EmployeeCreateCmd result = mapper.mapToEmployeeCreateCmd(employeeNameDTO);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping id to EmployeeDeleteCmd correctly")
  void mapToEmployeeDeleteCmdTest() {
    Long id = 1L;
    EmployeeDeleteCmd expected = new EmployeeDeleteCmd(id);
    EmployeeDeleteCmd result = mapper.mapToEmployeeDeleteCmd(id);

    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "1,Walter",
      "2,null"
  }, nullValues = {"null"})
  @DisplayName("Mapping id and EmployeeNameDTO to EmployeeUpdateCmd correctly")
  void mapToEmployeeUpdateCmdTest(Long id, String name) {
    EmployeeNameDTO employeeNameDTO = new EmployeeNameDTO(name);
    EmployeeUpdateCmd expected = new EmployeeUpdateCmd(id, name);
    EmployeeUpdateCmd result = mapper.mapToEmployeeUpdateCmd(id, employeeNameDTO);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping id to EmployeeByIdQuery correctly")
  void mapToEmployeeByIdQueryTest() {
    Long id = 1L;
    EmployeeByIdQuery expected = new EmployeeByIdQuery(id);
    EmployeeByIdQuery result = mapper.mapToEmployeeByIdQuery(id);

    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Mapping name to EmployeeByNameQuery correctly")
  void mapToEmployeeByNameQueryTest(String name) {
    EmployeeByNameQuery expected = new EmployeeByNameQuery(name);
    EmployeeByNameQuery result = mapper.mapToEmployeeByNameQuery(name);

    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "Walter,WALTER,6",
      "null,null,0"
  }, nullValues = {"null"})
  @DisplayName("Mapping Employee to EmployeeNameDetailsDTO correctly")
  void mapToDetailsDTOTest(String name, String expectedName, int expectedLength) {
    Employee employee = new Employee(1L, name);
    EmployeeNameDetailsDTO expected = new EmployeeNameDetailsDTO(1L, expectedName, expectedLength);

    EmployeeNameDetailsDTO result = mapper.mapToDetailsDTO(employee);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "1,Walter",
      "1,null"
  }, nullValues = {"null"})
  @DisplayName("Mapping Employee to EmployeeResponseDTO correctly")
  void mapToResponseDTOTest(Long number, String name) {
    Employee employee = new Employee(number, name);
    EmployeeResponseDTO expected = new EmployeeResponseDTO(number, name);
    EmployeeResponseDTO result = mapper.mapToResponseDTO(employee);

    Assertions.assertEquals(expected, result);
  }
}