package com.example.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Employee {

  private String nif;

  private String name;

  private String surname;

  private int birthYear;

  private Gender gender;

  private String companyPhone;

  private String personalPhone;

  private String company;

  private String email;
}
