package com.example.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Data
public class Phone {

  private String prefix;

  private String number;

  private PhoneType type;
}
