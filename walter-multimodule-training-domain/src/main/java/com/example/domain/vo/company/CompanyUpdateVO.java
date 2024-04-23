package com.example.domain.vo.company;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CompanyUpdateVO {

  String cif;

  String name;
}
