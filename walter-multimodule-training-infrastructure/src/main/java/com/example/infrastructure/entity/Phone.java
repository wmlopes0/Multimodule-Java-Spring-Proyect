package com.example.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Phone {

  private String prefix;

  private String number;

  private PhoneType type;
}
