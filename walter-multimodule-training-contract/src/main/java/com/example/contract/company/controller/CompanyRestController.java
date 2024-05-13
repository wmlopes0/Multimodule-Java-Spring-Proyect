package com.example.contract.company.controller;

import java.util.List;
import java.util.Optional;

import com.example.application.company.cmd.dto.CompanyCreateCmd;
import com.example.application.company.cmd.handler.CompanyCreateHandler;
import com.example.application.company.cmd.handler.CompanyDeleteHandler;
import com.example.application.company.cmd.handler.CompanyUpdateHandler;
import com.example.application.company.query.handler.CompanyGetByIdHandler;
import com.example.application.company.query.handler.CompanyListHandler;
import com.example.contract.company.mapper.CompanyContractMapper;
import com.example.domain.entity.Company;
import lombok.RequiredArgsConstructor;
import org.example.rest.api.CompanyManagementApi;
import org.example.rest.model.CompanyRequestDTO;
import org.example.rest.model.CompanyResponseDTO;
import org.example.rest.model.CompanyUpdateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
public class CompanyRestController implements CompanyManagementApi {

  private final CompanyCreateHandler companyCreateHandler;

  private final CompanyDeleteHandler companyDeleteHandler;

  private final CompanyUpdateHandler companyUpdateHandler;

  private final CompanyGetByIdHandler companyGetByIdHandler;

  private final CompanyListHandler companyListHandler;

  private final CompanyContractMapper mapper;

  public ResponseEntity<List<CompanyResponseDTO>> listCompanies() {
    List<CompanyResponseDTO> result = companyListHandler.listCompanies().stream()
        .map(mapper::mapToCompanyResponseDTO)
        .toList();
    return ResponseEntity.ok(result);
  }

  public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable("cif") String cif) {
    return Optional.ofNullable(companyGetByIdHandler.getCompanyById(
            mapper.mapToCompanyByIdQuery(cif)))
        .map(mapper::mapToCompanyResponseDTO)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  public ResponseEntity<CompanyResponseDTO> newCompany(@RequestBody CompanyRequestDTO companyRequest) {
    CompanyCreateCmd companyCreateCmd = mapper.mapToCompanyCreateCmd(companyRequest);
    Company company = companyCreateHandler.addCompany(companyCreateCmd);
    CompanyResponseDTO companyResponse = mapper.mapToCompanyResponseDTO(company);
    return ResponseEntity.status(HttpStatus.CREATED).body(companyResponse);
  }

  public ResponseEntity<CompanyResponseDTO> updateCompanyById(@PathVariable("cif") String cif,
      @RequestBody CompanyUpdateDTO companyRequest) {
    return Optional.ofNullable(companyUpdateHandler.updateCompany(
            mapper.mapToCompanyUpdateCmd(cif, companyRequest)))
        .map(mapper::mapToCompanyResponseDTO)
        .map(ResponseEntity::ok)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found."));
  }

  public ResponseEntity<Void> deleteCompanyById(@PathVariable("cif") String cif) {
    return companyDeleteHandler.deleteCompany(mapper.mapToCompanyDeleteCmd(cif))
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }
}
