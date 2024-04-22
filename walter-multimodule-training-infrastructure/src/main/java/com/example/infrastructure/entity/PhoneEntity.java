package com.example.infrastructure.entity;

import com.example.domain.entity.PhoneType;
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
public class PhoneEntity {

  private String prefix;

  private String number;

  private PhoneType type;
}
