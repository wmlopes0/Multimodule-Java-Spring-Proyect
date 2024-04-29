package com.example.contract.company.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CompanyResponseDTO {

  private String cif;

  private String name;

  private List<EmployeeDTO> employees;

}
