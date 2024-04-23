package com.example.infrastructure.mapper.company;

import java.util.List;
import java.util.stream.Stream;

import com.example.domain.entity.Company;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import com.example.infrastructure.entity.CompanyEntity;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.entity.PhoneEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CompanyInfrastructureMapperImplTest {

  private final CompanyInfrastructureMapper companyInfrastructureMapper = new CompanyInfrastructureMapperImpl();

  @ParameterizedTest
  @MethodSource("listEmployeesParameters")
  @DisplayName("Mapping CompanyEntity to Company correctly")
  void mapToDomainTest(List<EmployeeEntity> employeeEntities, List<Employee> employees) {
    CompanyEntity companyEntity = new CompanyEntity()
        .setCif("V33778580")
        .setName("Company S.L")
        .setEmployees(employeeEntities);
    Company expected = new Company()
        .setCif("V33778580")
        .setName("Company S.L")
        .setEmployees(employees);

    Company result = companyInfrastructureMapper.mapToDomain(companyEntity);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("listEmployeesParameters")
  @DisplayName("Mapping CompanyCreateVO to CompanyEntity correctly")
  void mapComanyCreateVOToEntityTest(List<EmployeeEntity> employeeEntities, List<Employee> employees) {
    CompanyCreateVO companyCreateVO = CompanyCreateVO.builder()
        .cif("V33778580")
        .name("Company S.L")
        .employees(employees)
        .build();
    CompanyEntity expected = new CompanyEntity()
        .setCif("V33778580")
        .setName("Company S.L")
        .setEmployees(employeeEntities);

    CompanyEntity result = companyInfrastructureMapper.mapToEntity(companyCreateVO);
    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping CompanyUpdateVO to CompanyEntity correctly")
  void mapComanyUpdateVOToEntityTest() {
    CompanyUpdateVO companyUpdateVO = CompanyUpdateVO.builder()
        .cif("V33778580")
        .name("Company S.L")
        .build();
    CompanyEntity expected = new CompanyEntity()
        .setCif("V33778580")
        .setName("Company S.L");

    CompanyEntity result = companyInfrastructureMapper.mapToEntity(companyUpdateVO);
    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> listEmployeesParameters() {
    return Stream.of(
        Arguments.of(
            List.of(
                new EmployeeEntity()
                    .setNif("45134320V")
                    .setName("Walter")
                    .setLastName("Martín Lopes")
                    .setBirthYear(1998)
                    .setGender(Gender.MALE.getCode())
                    .setPhones(List.of(
                        new PhoneEntity("+34", "722748406", PhoneType.PERSONAL),
                        new PhoneEntity("+34", "676615106", PhoneType.COMPANY)
                    ))
                    .setEmail("wmlopes0@gmail.com"),
                new EmployeeEntity()
                    .setNif("45132337N")
                    .setName("Raquel")
                    .setLastName("Barbero Sánchez")
                    .setBirthYear(1996)
                    .setGender(Gender.FEMALE.getCode())
                    .setPhones(List.of(
                        new PhoneEntity("+34", "676615106", PhoneType.PERSONAL),
                        new PhoneEntity("+34", "722748406", PhoneType.COMPANY)
                    ))
                    .setEmail("raquelbarberosanchez90@gmail.com")
            ),
            List.of(
                new Employee()
                    .setNif("45134320V")
                    .setName("Walter")
                    .setSurname("Martín Lopes")
                    .setBirthYear(1998)
                    .setGender(Gender.MALE)
                    .setPersonalPhone("+34722748406")
                    .setCompanyPhone("+34676615106")
                    .setEmail("wmlopes0@gmail.com"),
                new Employee()
                    .setNif("45132337N")
                    .setName("Raquel")
                    .setSurname("Barbero Sánchez")
                    .setBirthYear(1996)
                    .setGender(Gender.FEMALE)
                    .setPersonalPhone("+34676615106")
                    .setCompanyPhone("+34722748406")
                    .setEmail("raquelbarberosanchez90@gmail.com")
            )
        )
    );
  }
}