package com.example.service;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository repository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    @DisplayName("Return all employees correctly")
    void listAllEmployeesTest() {
        Mockito.when(repository.findAll()).thenReturn(List.of(new Employee(1L, "Pepe"), new Employee(2L, "Walter")));

        List<Employee> result = employeeService.listAllEmployees();
        List<Employee> expected = List.of(new Employee(1L, "Pepe"), new Employee(2L, "Walter"));

        Assertions.assertEquals(expected, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("List all employees throws RuntimeException on repository error")
    void listAllEmployeesErrorTest() {
        Mockito.when(repository.findAll()).thenThrow(new RuntimeException("Error"));
        Assertions.assertThrows(RuntimeException.class, () -> employeeService.listAllEmployees());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Add and update employees correctly")
    void addOrUpdateEmployeeTest() {
        Employee newEmployee = new Employee(1L, "Walter");
        employeeService.addEmployee(newEmployee);
        verify(repository, times(1)).save(newEmployee);
    }

    @Test
    @DisplayName("Delete employees correctly")
    void deleteEmployeeByIdTest() {
        Long id = 1L;
        employeeService.deleteEmployeeById(id);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Find employees by name correctly")
    void firstEmployeeContainsNameTest() {
        String namePart = "Wal";
        Employee expected = new Employee(1L, "Walter");
        Mockito.when(repository.findFirstByNameContainingIgnoreCase(namePart)).thenReturn(Optional.of(expected));
        Employee result = employeeService.firstEmployeeContainsName(namePart);
        Assertions.assertEquals(expected, result);
        verify(repository, times(1)).findFirstByNameContainingIgnoreCase(namePart);
    }

    @Test
    @DisplayName("Find employees by name return null when not found")
    void firstEmployeeContainsNameNotFoundTest() {
        String namePart = "Not found";
        Mockito.when(repository.findFirstByNameContainingIgnoreCase(namePart)).thenReturn(Optional.empty());
        Employee result = employeeService.firstEmployeeContainsName(namePart);
        Assertions.assertNull(result);
        verify(repository, times(1)).findFirstByNameContainingIgnoreCase(namePart);
    }
}