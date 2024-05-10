package com.example.contract.company.controller;

import java.util.List;
import java.util.Optional;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.application.company.cmd.handler.CompanyCreateHandler;
import com.example.application.company.cmd.handler.CompanyDeleteHandler;
import com.example.application.company.cmd.handler.CompanyUpdateHandler;
import com.example.application.company.query.handler.CompanyGetByIdHandler;
import com.example.application.company.query.handler.CompanyListHandler;
import com.example.contract.company.dto.CompanyRequestDTO;
import com.example.contract.company.dto.CompanyResponseDTO;
import com.example.contract.company.dto.CompanyUpdateDTO;
import com.example.contract.company.mapper.CompanyContractMapper;
import com.example.domain.entity.Company;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Company Management", description = "Handling of company data")
@RequiredArgsConstructor
@RestController
@RequestMapping("/companies")
public class CompanyRestController {

  private final CompanyCreateHandler companyCreateHandler;

  private final CompanyDeleteHandler companyDeleteHandler;

  private final CompanyUpdateHandler companyUpdateHandler;

  private final CompanyGetByIdHandler companyGetByIdHandler;

  private final CompanyListHandler companyListHandler;

  private final CompanyContractMapper mapper;

  @Operation(summary = "More information...", description = "This endpoint lists all companies in the database")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @GetMapping("/")
  public ResponseEntity<List<CompanyResponseDTO>> listCompanies() {
    List<CompanyResponseDTO> result = companyListHandler.listCompanies().stream()
        .map(mapper::mapToCompanyResponseDTO)
        .toList();
    return ResponseEntity.ok(result);
  }

  @Operation(summary = "More information...", description = "This endpoint obtains information about an companies by their CIF")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Bad request due to id not found")
  @GetMapping("/{cif}")
  public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable("cif") String cif) {
    return Optional.ofNullable(companyGetByIdHandler.getCompanyById(
            mapper.mapToCompanyByIdQuery(cif)))
        .map(mapper::mapToCompanyResponseDTO)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @Operation(summary = "More information...", description = "This endpoint gets adds an Company to the database")
  @ApiResponse(responseCode = "201", description = "Successful operation")
  @PostMapping("/")
  public ResponseEntity<CompanyResponseDTO> newCompany(@RequestBody CompanyRequestDTO companyRequest) {
    CompanyCreateCmd companyCreateCmd = mapper.mapToCompanyCreateCmd(companyRequest);
    Company company = companyCreateHandler.addCompany(companyCreateCmd);
    CompanyResponseDTO companyResponse = mapper.mapToCompanyResponseDTO(company);
    return ResponseEntity.status(HttpStatus.CREATED).body(companyResponse);
  }

  @Operation(summary = "More information...", description = "This endpoint updates information for a given company")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Bad request due to id not found")
  @PutMapping("/{cif}")
  public ResponseEntity<CompanyResponseDTO> updateCompanyById(@PathVariable("cif") String cif,
      @RequestBody CompanyUpdateDTO companyRequest) {
    return Optional.ofNullable(companyUpdateHandler.updateCompany(
            mapper.mapToCompanyUpdateCmd(cif, companyRequest)))
        .map(mapper::mapToCompanyResponseDTO)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found."));
  }

  @Operation(summary = "More information...", description = "This endpoint removes a given company from the database by their id")
  @ApiResponse(responseCode = "200", description = "Successful operation")
  @ApiResponse(responseCode = "404", description = "Bad request due to id not found")
  @DeleteMapping("/{cif}")
  public ResponseEntity<Object> deleteCompanyById(@PathVariable("cif") String cif) {
    return companyDeleteHandler.deleteCompany(mapper.mapToCompanyDeleteCmd(cif))
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }
}
