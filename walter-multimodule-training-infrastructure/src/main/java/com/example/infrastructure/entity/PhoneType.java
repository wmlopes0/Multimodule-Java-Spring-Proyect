package com.example.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PhoneType {
  COMPANY(1),
  PERSONAL(2);

  private final int type;
}
