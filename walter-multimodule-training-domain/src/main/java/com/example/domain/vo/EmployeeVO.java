package com.example.domain.vo;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class EmployeeVO {

  Long number;

  String name;
}
