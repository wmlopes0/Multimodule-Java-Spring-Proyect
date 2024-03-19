package com.example.service;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository repository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

//    @BeforeEach //Se ejecuta cada vez que se ejecuta un test
//    void init() {
//        repository = Mockito.mock(EmployeeRepository.class); //Instancia ficticia
//        employeeService = new EmployeeServiceImpl(repository);
//    }


    @Test
    void listAllEmployeesTest() {
        Mockito.when(repository.findAll()).thenReturn(List.of(new Employee(1l, "Pepe"), new Employee(2l, "Walter")));

        List<Employee> result = employeeService.listAllEmployees();
        List<Employee> expected = List.of(new Employee(1l, "Pepe"), new Employee(2l, "Walter"));

        Assertions.assertEquals(expected, result);
    }

    @Test
    void listAllEmployeesErrorTest() {
        Mockito.when(repository.findAll()).thenThrow(new RuntimeException("Error"));
        Assertions.assertThrows(RuntimeException.class, () -> employeeService.listAllEmployees());
    }

}