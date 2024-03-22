package com.example.mapper;

import com.example.model.Employee;
import com.example.model.EmployeeNameDetailsDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class EmployeeMapperImplTest {
    private final EmployeeMapperImpl employeeMapper = new EmployeeMapperImpl();

    @ParameterizedTest
    @CsvSource(value = {
            "Walter,WALTER,6",
            "null,null,0"
    }, nullValues = {"null"})
    @DisplayName("Mapping Employee to EmployeeNameDetailsDTO correctly")
    void toDetailsDTO(String name, String expectedName, int expectedLength) {
        Employee employee = new Employee(1L, name);
        EmployeeNameDetailsDTO expected = new EmployeeNameDetailsDTO(1L, expectedName, expectedLength);

        EmployeeNameDetailsDTO result = employeeMapper.toDetailsDTO(employee);
        Assertions.assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("parameters")
    @DisplayName("Mapping Employee to EmployeeNameDetailsDTO correctly")
    void toDetailsDTOParametized(Employee employee, EmployeeNameDetailsDTO expected) {
        EmployeeNameDetailsDTO result = employeeMapper.toDetailsDTO(employee);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(
                        new Employee(1L, "Walter"),
                        new EmployeeNameDetailsDTO(1L, "WALTER", 6)
                ),
                Arguments.of(
                        new Employee(1L, null),
                        new EmployeeNameDetailsDTO(1L, null, 0)
                )
        );
    }
}