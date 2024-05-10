package com.example.infrastructure.config;

import com.example.infrastructure.mapper.company.CompanyInfrastructureMapper;
import com.example.infrastructure.mapper.company.CompanyInfrastructureMapperImpl;
import com.example.infrastructure.mapper.employee.EmployeeInfrastructureMapper;
import com.example.infrastructure.mapper.employee.EmployeeInfrastructureMapperImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
  @Bean
  public CompanyInfrastructureMapper companyInfrastructureMapper() {
    return new CompanyInfrastructureMapperImpl();
  }

  @Bean
  public EmployeeInfrastructureMapper employeeInfrastructureMapper() {
    return new EmployeeInfrastructureMapperImpl();
  }
}
