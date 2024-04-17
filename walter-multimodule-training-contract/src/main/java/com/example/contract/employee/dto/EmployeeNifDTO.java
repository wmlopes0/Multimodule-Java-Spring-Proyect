package com.example.contract.employee.dto;

import com.example.domain.validation.ValidNIF;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
public class EmployeeNifDTO {

  @NotNull(message = "NIF cannot be null")
  @ValidNIF
  private String nif;
}
