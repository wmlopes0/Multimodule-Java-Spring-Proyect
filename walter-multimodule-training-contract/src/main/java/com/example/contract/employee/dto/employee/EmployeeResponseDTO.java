package com.example.contract.employee.dto.employee;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class EmployeeResponseDTO {

  private String nif;

  private String completeName;

  private int birthYear;

  private int age;

  private boolean adult;

  private String gender;

  private List<PhoneDTO> phones;

  private String email;
}
