package com.example.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
  MALE(1),
  FEMALE(2);

  private final int code;

}
