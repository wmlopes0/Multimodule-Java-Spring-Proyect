package com.example.contract.employee.dto.company;

import com.example.contract.employee.validation.ValidGender;
import com.example.contract.employee.validation.ValidNIF;
import com.example.contract.employee.validation.ValidPhone;
import com.example.contract.employee.validation.ValidYearOfBirth;
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
public class EmployeeDTO {

  private String nif;

  private String name;

  private String surname;

  private Integer birthYear;

  private String gender;

  private String personalPhone;

  private String companyPhone;

  private String email;

}
