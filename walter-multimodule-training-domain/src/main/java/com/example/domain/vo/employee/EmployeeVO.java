package com.example.domain.vo.employee;

import com.example.domain.entity.Gender;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class EmployeeVO {

  String nif;

  String name;

  String surname;

  int birthYear;

  Gender gender;

  String companyPhone;

  String personalPhone;

  String company;

  String email;
}
