package com.example.infrastructure.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Document("employees")
public class EmployeeEntity {

  @Id
  private String nif;

  private String name;

  private String lastName;

  private int birthYear;

  private int gender;

  private List<Phone> phones;

  private String email;
}
