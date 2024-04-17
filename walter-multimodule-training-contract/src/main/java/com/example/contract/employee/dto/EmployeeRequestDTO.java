package com.example.contract.employee.dto;

import com.example.domain.validation.ValidGender;
import com.example.domain.validation.ValidNIF;
import com.example.domain.validation.ValidPhone;
import com.example.domain.validation.ValidYearOfBirth;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class EmployeeRequestDTO {

  @NotNull(message = "NIF cannot be null")
  @ValidNIF
  private String nif;

  @NotNull(message = "Name cannot be null")
  @Size(min = 1, message = "Name cannot be empty")
  private String name;

  private String surname;

  @NotNull(message = "Birth year cannot be null")
  @ValidYearOfBirth
  private Integer birthYear;

  @NotNull(message = "Gender cannot be null")
  @ValidGender(message = "Gender must be either 'Male' or 'Female'")
  private String gender;

  @NotNull(message = "Personal phone cannot be null")
  @Size(min = 1, message = "Personal phone cannot be empty")
  @ValidPhone
  private String personalPhone;

  @ValidPhone
  private String companyPhone;

  @Email(message = "Invalid email format")
  private String email;
}
