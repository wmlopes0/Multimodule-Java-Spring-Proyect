package com.example.domain.vo.company;

import java.util.List;

import com.example.domain.entity.Employee;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CompanyCreateVO {

  String cif;

  String name;

  List<Employee> employees;
}
