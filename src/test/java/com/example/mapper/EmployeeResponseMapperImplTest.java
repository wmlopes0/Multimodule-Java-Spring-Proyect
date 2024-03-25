package com.example.mapper;

import com.example.model.Employee;
import com.example.model.EmployeeResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
class EmployeeResponseMapperImplTest {
    private final EmployeeResponseMapperImpl employeeResponseMapper = new EmployeeResponseMapperImpl();

    @ParameterizedTest
    @CsvSource(value = {
            "1,Walter",
            "1,null"
    }, nullValues = {"null"})
    @DisplayName("Mapping Employee to EmployeeResponseDTO correctly")
    void toResponseDTO(Long number, String name) {
        Employee employee = new Employee(number, name);
        EmployeeResponseDTO expected = new EmployeeResponseDTO(number, name);
        EmployeeResponseDTO result = employeeResponseMapper.toResponseDTO(employee);

        Assertions.assertEquals(expected, result);
    }
}