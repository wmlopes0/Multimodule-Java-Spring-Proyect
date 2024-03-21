package com.example.mapper;

import com.example.model.Employee;
import com.example.model.EmployeeNameDetailsDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeMapperImplTest {

    private final EmployeeMapperImpl employeeMapper = new EmployeeMapperImpl();

    @Test
    @DisplayName("Mapping Employee to EmployeeNameDetailsDTO correctly")
    void toDetailsDTO() {
        Employee employee = new Employee(1L, "Walter");
        EmployeeNameDetailsDTO expected = new EmployeeNameDetailsDTO(1L, "WALTER", 6);

        EmployeeNameDetailsDTO result = employeeMapper.toDetailsDTO(employee);
        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Mapping Employee to EmployeeNameDetailsDTO correctly when name is null")
    void toDetailsDTONameNull() {
        Employee employee = new Employee(1L, null);
        EmployeeNameDetailsDTO expected = new EmployeeNameDetailsDTO(1L, null, 0);

        EmployeeNameDetailsDTO result = employeeMapper.toDetailsDTO(employee);
        Assertions.assertEquals(expected, result);
    }

}