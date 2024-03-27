package com.example.controller;

import java.util.List;
import java.util.Optional;

import com.example.cmd.EmployeeCreateCmd;
import com.example.domain.Employee;
import com.example.dto.EmployeeNameDTO;
import com.example.dto.EmployeeNameDetailsDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.mapper.EmployeeControllerMapper;
import com.example.query.EmployeeByNameQuery;
import com.example.service.EmployeeService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@OpenAPIDefinition(info = @Info(title = "Employees API", version = "1.0",
    description = "This is my first API with Spring Boot, a simple example with crud operations and documentation with Swagger"))
@RequiredArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeRestController {

  private final EmployeeService employeeService;

  private final EmployeeControllerMapper employeeControllerMapper;

  @Operation(summary = "More information...", description = "This endpoint lists all employees in the database")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @GetMapping("/")
  public ResponseEntity<List<EmployeeNameDetailsDTO>> listEmployees() {
    List<EmployeeNameDetailsDTO> result = employeeService.listEmployees().stream()
        .map(employeeControllerMapper::mapToDetailsDTO)
        .toList();
    return ResponseEntity.ok(result);
  }

  @Operation(summary = "More information...", description = "This endpoint obtains information about an employee by their ID")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Bad request due to id not found")
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeNameDetailsDTO> getEmployeeById(@PathVariable("id") Long id) {
    return Optional.ofNullable(employeeService.getEmployeeById(id))
        .map(employeeControllerMapper::mapToDetailsDTO)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "More information...",
      description = "This endpoint gets information about an employee that contains that string in their name")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Bad request due to no match found")
  @GetMapping("/name/{name}")
  public ResponseEntity<EmployeeNameDetailsDTO> getEmployeeByName(@PathVariable("name") String name) {
    EmployeeByNameQuery employeeByNameQuery = new EmployeeByNameQuery(name);
    return Optional.ofNullable(employeeService.getEmployeeByName(employeeByNameQuery))
        .map(employeeControllerMapper::mapToDetailsDTO)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "More information...", description = "This endpoint gets adds an employee to the database")
  @ApiResponse(responseCode = "201", description = "Successful operation")
  @PostMapping("/")
  public ResponseEntity<EmployeeResponseDTO> newEmployee(@RequestBody EmployeeNameDTO employeeRequest) {
    EmployeeCreateCmd employeeCreateCmd = employeeControllerMapper.mapToEmployeeCreateCmd(employeeRequest);
    Employee employee = employeeService.addEmployee(employeeCreateCmd);
    EmployeeResponseDTO employeeResponse = employeeControllerMapper.mapToResponseDTO(employee);
    return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponse);
  }

  @Operation(summary = "More information...", description = "This endpoint updates information for a given employee")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Bad request due to id not found")
  @PutMapping("/{id}")
  public ResponseEntity<EmployeeResponseDTO> updateEmployeeById(@PathVariable("id") Long id, @RequestBody EmployeeNameDTO employeeUpdate) {
    return Optional.ofNullable(employeeService.updateEmployeeById(employeeControllerMapper.mapToEmployeeUpdateCmd(id, employeeUpdate)))
        .map(employeeControllerMapper::mapToResponseDTO)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con ID: " + id));
  }

  @Operation(summary = "More information...", description = "This endpoint removes a given employee from the database by their id")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Bad request due to id not found")
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteEmployeeById(@PathVariable("id") Long id) {
    return employeeService.deleteEmployeeById(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
  }
}
