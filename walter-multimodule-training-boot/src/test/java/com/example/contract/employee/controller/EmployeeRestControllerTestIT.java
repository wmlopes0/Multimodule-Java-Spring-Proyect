package com.example.contract.employee.controller;

import java.util.List;

import com.example.boot.app.App;
import com.example.contract.employee.dto.EmployeeNameDetailsDTO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest(classes = App.class, properties = {"spring.profiles.active = test"})
class EmployeeRestControllerTestIT {

  @Autowired
  private EmployeeRestController controller;

  @Autowired
  private EmployeeRepository repository;

  @BeforeEach
  void init() {
    repository.deleteAll();
  }

  @Test
  void listEmployeesTest() {
    Long id1 = repository.save(new EmployeeEntity().setName("Walter")).getNumber();
    Long id2 = repository.save(new EmployeeEntity().setName("Quique")).getNumber();
    ResponseEntity<List<EmployeeNameDetailsDTO>> expected = ResponseEntity.ok(
        List.of(new EmployeeNameDetailsDTO(id1, "WALTER", 6),
            new EmployeeNameDetailsDTO(id2, "QUIQUE", 6)));

    ResponseEntity<List<EmployeeNameDetailsDTO>> result = controller.listEmployees();
    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

  @Test
  void listEmployeesEmptyTest() {
    ResponseEntity<List<EmployeeNameDetailsDTO>> expected = ResponseEntity.ok(List.of());
    ResponseEntity<List<EmployeeNameDetailsDTO>> result = controller.listEmployees();
    Assertions.assertEquals(expected.getStatusCode(), result.getStatusCode());
    Assertions.assertEquals(expected.getBody(), result.getBody());
  }

}

