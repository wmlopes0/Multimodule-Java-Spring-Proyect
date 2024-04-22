package com.example.domain.vo.employee;

import com.example.domain.entity.Gender;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class EmployeeVO {

  private String nif;

  private String name;

  private String surname;

  private int birthYear;

  private Gender gender;

  private String companyPhone;

  private String personalPhone;

  private String email;
}
