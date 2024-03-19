package com.example.service;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository repository;


    @Override
    public List<Employee> listAllEmployees() {
        return repository.findAll();
    }

    @Override
    public void addEmployee(Employee emp) {
        repository.save(emp);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Employee firstEmployeeContainsName(String namePart) {
        return repository.findFirstByNameContainingIgnoreCase(namePart).orElse(null);
    }
}
