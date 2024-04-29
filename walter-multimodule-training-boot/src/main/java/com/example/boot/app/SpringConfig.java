package com.example.boot.app;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.infrastructure.repository")
@ComponentScan(basePackages = {
    "com.example.boot",
    "com.example.contract",
    "com.example.application",
    "com.example.domain",
    "com.example.infrastructure"})
@EntityScan("com.example.infrastructure.entity")
public class SpringConfig {

  @Bean
  public GroupedOpenApi employeeApi() {
    return GroupedOpenApi.builder()
        .group("springdoc")
        .packagesToScan("com.example.contract.employee.controller")
        .build();
  }

  @Bean
  public GroupedOpenApi companyApi() {
    return GroupedOpenApi.builder()
        .group("springdoc")
        .packagesToScan("com.example.contract.company.controller")
        .build();
  }
}
