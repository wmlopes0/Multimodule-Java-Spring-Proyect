package com.example.infrastructure.entity;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Document("companies")
public class CompanyEntity {

  @Id
  private String cif;

  @NotNull(message = "Name cannot be null")
  private String name;

  private List<EmployeeEntity> employees;
}
