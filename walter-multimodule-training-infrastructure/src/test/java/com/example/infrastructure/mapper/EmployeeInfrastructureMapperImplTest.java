package com.example.infrastructure.mapper;

import java.util.stream.Stream;

import com.example.domain.entity.Employee;
import com.example.infrastructure.entity.EmployeeEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class EmployeeInfrastructureMapperImplTest {

  private final EmployeeInfrastructureMapper employeeInfrastructureMapper = new EmployeeInfrastructureMapperImpl();

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Mapping EmployeeEntity to Employee correctly")
  void mapToDomainTest(String name) {
    EmployeeEntity employeeEntity = new EmployeeEntity(1L, name);
    Employee expected = new Employee(1L, name);
    Employee result = employeeInfrastructureMapper.mapToDomain(employeeEntity);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "Walter",
      "null"
  }, nullValues = {"null"})
  @DisplayName("Mapping EmployeeNameVO to EmployeeEntity correctly")
  void mapToEntityTest(String name) {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name(name).build();
    EmployeeEntity expected = new EmployeeEntity().setName(name);
    EmployeeEntity result = employeeInfrastructureMapper.mapToEntity(employeeNameVO);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("parameters")
  @DisplayName("Mapping EmployeeUpdateVO to EmployeeEntity correctly")
  void mapToEntityTest(EmployeeEntity expected, EmployeeUpdateVO employeeUpdateVO) {
    EmployeeEntity result = employeeInfrastructureMapper.mapToEntity(employeeUpdateVO);
    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> parameters() {
    return Stream.of(
        Arguments.of(
            new EmployeeEntity(1L, "Walter"),
            EmployeeUpdateVO.builder().number(1L).name("Walter").build()
        ),
        Arguments.of(
            new EmployeeEntity(1L, null),
            EmployeeUpdateVO.builder().number(1L).name(null).build()
        )
    );
  }
}