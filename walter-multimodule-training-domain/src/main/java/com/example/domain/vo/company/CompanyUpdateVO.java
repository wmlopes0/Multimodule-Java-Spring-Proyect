package com.example.domain.vo.company;

import java.util.List;

import com.example.domain.entity.Employee;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CompanyUpdateVO {

  private String cif;

  private String name;

  private List<Employee> employees;
}
