package com.example.contract.employee.controller;

import java.util.List;
import java.util.Optional;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.handler.EmployeeCreateHandler;
import com.example.application.employee.cmd.handler.EmployeeDeleteHandler;
import com.example.application.employee.cmd.handler.EmployeeUpdateHandler;
import com.example.application.employee.query.handler.EmployeeGetByIdHandler;
import com.example.application.employee.query.handler.EmployeeGetByNameHandler;
import com.example.application.employee.query.handler.EmployeeListHandler;
import com.example.contract.employee.dto.EmployeeNameDTO;
import com.example.contract.employee.dto.EmployeeNameDetailsDTO;
import com.example.contract.employee.dto.EmployeeResponseDTO;
import com.example.contract.employee.mapper.EmployeeContractMapper;
import com.example.domain.entity.Employee;
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

  private final EmployeeCreateHandler employeeCreateHandler;

  private final EmployeeDeleteHandler employeeDeleteHandler;

  private final EmployeeUpdateHandler employeeUpdateHandler;

  private final EmployeeGetByIdHandler employeeGetByIdHandler;

  private final EmployeeGetByNameHandler employeeGetByNameHandler;

  private final EmployeeListHandler employeeListHandler;

  private final EmployeeContractMapper mapper;

  @Operation(summary = "More information...", description = "This endpoint lists all employees in the database")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @GetMapping("/")
  public ResponseEntity<List<EmployeeNameDetailsDTO>> listEmployees() {
    try {
      List<EmployeeNameDetailsDTO> result = employeeListHandler.listEmployees().stream()
          .map(mapper::mapToDetailsDTO)
          .toList();
      return ResponseEntity.ok(result);
    } catch (RuntimeException e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @Operation(summary = "More information...", description = "This endpoint obtains information about an employee by their ID")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Bad request due to id not found")
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeNameDetailsDTO> getEmployeeById(@PathVariable("id") Long id) {
    return Optional.ofNullable(employeeGetByIdHandler.getEmployeeById(
            mapper.mapToEmployeeByIdQuery(id)))
        .map(mapper::mapToDetailsDTO)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "More information...",
      description = "This endpoint gets information about an employee that contains that string in their name")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Bad request due to no match found")
  @GetMapping("/name/{name}")
  public ResponseEntity<EmployeeNameDetailsDTO> getEmployeeByName(@PathVariable("name") String name) {
    return Optional.ofNullable(employeeGetByNameHandler.getEmployeeByName(
            mapper.mapToEmployeeByNameQuery(name)))
        .map(mapper::mapToDetailsDTO)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "More information...", description = "This endpoint gets adds an employee to the database")
  @ApiResponse(responseCode = "201", description = "Successful operation")
  @PostMapping("/")
  public ResponseEntity<EmployeeResponseDTO> newEmployee(@RequestBody EmployeeNameDTO employeeRequest) {
    EmployeeCreateCmd employeeCreateCmd = mapper.mapToEmployeeCreateCmd(employeeRequest);
    Employee employee = employeeCreateHandler.addEmployee(employeeCreateCmd);
    EmployeeResponseDTO employeeResponse = mapper.mapToResponseDTO(employee);
    return ResponseEntity.status(HttpStatus.CREATED).body(employeeResponse);
  }

  @Operation(summary = "More information...", description = "This endpoint updates information for a given employee")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Bad request due to id not found")
  @PutMapping("/{id}")
  public ResponseEntity<EmployeeResponseDTO> updateEmployeeById(@PathVariable("id") Long id, @RequestBody EmployeeNameDTO employeeUpdate) {
    return Optional.ofNullable(employeeUpdateHandler.updateEmployee(
            mapper.mapToEmployeeUpdateCmd(id, employeeUpdate)))
        .map(mapper::mapToResponseDTO)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empleado no encontrado con ID: " + id));
  }

  @Operation(summary = "More information...", description = "This endpoint removes a given employee from the database by their id")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Bad request due to id not found")
  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteEmployeeById(@PathVariable("id") Long id) {
    return employeeDeleteHandler.deleteEmployee(mapper.mapToEmployeeDeleteCmd(id))
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }
}
