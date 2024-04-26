package com.example.contract.company.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
public class EmployeeDTO {

  private String nif;

  private String name;

  private String surname;

  private Integer birthYear;

  private String gender;

  private String personalPhone;

  private String companyPhone;

  private String company;

  private String email;

}
