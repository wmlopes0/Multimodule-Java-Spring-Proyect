package com.example.domain.vo.company;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CompanyCreateVO {

  String cif;

  String name;
}
