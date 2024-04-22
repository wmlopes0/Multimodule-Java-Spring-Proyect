package com.example.infrastructure.mapper.employee;

import java.util.List;
import java.util.stream.Stream;

import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
import com.example.domain.vo.employee.EmployeeNameVO;
import com.example.domain.vo.employee.EmployeeNifVO;
import com.example.domain.vo.employee.EmployeeVO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.entity.PhoneEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class EmployeeInfrastructureMapperImplTest {

  private final EmployeeInfrastructureMapper employeeInfrastructureMapper = new EmployeeInfrastructureMapperImpl();

  @ParameterizedTest
  @MethodSource("mapToDomainParameters")
  @DisplayName("Mapping EmployeeEntity to Employee correctly")
  void mapToDomainTest(List<PhoneEntity> phones, String expectedPersonalPhone, String expectedCompanyPhone) {
    EmployeeEntity employeeEntity = new EmployeeEntity()
        .setNif("45134320V")
        .setName("Walter")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(phones)
        .setEmail("wmlopes0@gmail.com");

    Employee expected = new Employee()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE)
        .setCompanyPhone(expectedCompanyPhone)
        .setPersonalPhone(expectedPersonalPhone)
        .setEmail("wmlopes0@gmail.com");

    Employee result = employeeInfrastructureMapper.mapToDomain(employeeEntity);

    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("mapToDomainParameters")
  @DisplayName("Mapping Employee to EmployeeEntity correctly")
  void mapDomainToEntity(List<PhoneEntity> expectedPhones, String personalPhone, String companyPhone) {
    Employee employee = new Employee()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE)
        .setCompanyPhone(companyPhone)
        .setPersonalPhone(personalPhone)
        .setEmail("wmlopes0@gmail.com");

    EmployeeEntity expected = new EmployeeEntity()
        .setNif("45134320V")
        .setName("Walter")
        .setLastName("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE.getCode())
        .setPhones(expectedPhones)
        .setEmail("wmlopes0@gmail.com");

    EmployeeEntity result = employeeInfrastructureMapper.mapDomainToEntity(employee);

    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> mapToDomainParameters() {
    return Stream.of(
        Arguments.of(
            List.of(
                new PhoneEntity("+34", "722748406", PhoneType.PERSONAL),
                new PhoneEntity("+34", "676615106", PhoneType.COMPANY)
            ),
            "+34722748406",
            "+34676615106"
        ),
        Arguments.of(
            List.of(
                new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)
            ),
            "+34722748406",
            null
        ),
        Arguments.of(
            List.of(),
            null,
            null
        )
    );
  }

  @Test
  @DisplayName("Mapping EmployeeNameVO to EmployeeEntity correctly")
  void mapEmployeeNameVOToEntityTest() {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();
    EmployeeEntity expected = new EmployeeEntity().setName("Walter");
    EmployeeEntity result = employeeInfrastructureMapper.mapToEntity(employeeNameVO);
    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping EmployeeNifVO to EmployeeEntity correctly")
  void mapEmployeeNifVOToEntityTest() {
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder().nif("45134320V").build();
    EmployeeEntity expected = new EmployeeEntity().setNif("45134320V");
    EmployeeEntity result = employeeInfrastructureMapper.mapToEntity(employeeNifVO);
    Assertions.assertEquals(expected, result);
  }

  @ParameterizedTest
  @MethodSource("employeeVOToEntityParameters")
  @DisplayName("Mapping EmployeeVO to EmployeeEntity correctly")
  void mapEmployeeVOToEntityTest(EmployeeVO employeeVO, EmployeeEntity expected) {
    EmployeeEntity result = employeeInfrastructureMapper.mapToEntity(employeeVO);
    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> employeeVOToEntityParameters() {
    return Stream.of(
        Arguments.of(
            EmployeeVO.builder()
                .nif("45134320V")
                .name("Walter")
                .surname("Martín Lopes")
                .birthYear(1998)
                .gender(Gender.MALE)
                .personalPhone("+34722748406")
                .companyPhone("+34676615106")
                .email("wmlopes0@gmail.com").build(),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(
                    new PhoneEntity("+34", "676615106", PhoneType.COMPANY),
                    new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com")
        ),
        Arguments.of(
            EmployeeVO.builder()
                .nif("45134320V")
                .name("Walter")
                .birthYear(1998)
                .gender(Gender.MALE)
                .companyPhone("+34676615106")
                .email("wmlopes0@gmail.com").build(),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(
                    new PhoneEntity("+34", "676615106", PhoneType.COMPANY)))
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @ParameterizedTest
  @CsvSource(value = {
      "1, MALE",
      "2, FEMALE"
  })
  @DisplayName("Mapping gender code to Gender correctly")
  void mapToGenderTest(int genderCode, Gender expectedGender) {
    Gender result = employeeInfrastructureMapper.mapToGender(genderCode);
    Assertions.assertEquals(expectedGender, result);
  }

  @ParameterizedTest
  @CsvSource(value = {
      "3 , ",
      "4 , ",
  })
  @DisplayName("Test IllegalArgumentException for invalid gender codes")
  void mapToGenderErrorTest(int genderCode) {
    Assertions.assertThrows(IllegalArgumentException.class, () -> employeeInfrastructureMapper.mapToGender(genderCode));
  }

  @ParameterizedTest
  @MethodSource("extractPhoneParameters")
  @DisplayName("Extract full phone number to List for phone type correctly")
  void extractPhoneWithTypeOfListTest(List<PhoneEntity> phones, PhoneType phoneType, String expectedPhone) {
    String result = employeeInfrastructureMapper.extractPhoneWithTypeOfList(phones, phoneType);
    Assertions.assertEquals(expectedPhone, result);
  }

  private static Stream<Arguments> extractPhoneParameters() {
    return Stream.of(
        Arguments.of(
            List.of(
                new PhoneEntity("+34", "722748406", PhoneType.PERSONAL),
                new PhoneEntity("+34", "676615106", PhoneType.COMPANY)
            ),
            PhoneType.PERSONAL,
            "+34722748406"
        ),
        Arguments.of(
            List.of(
                new PhoneEntity("+34", "722748406", PhoneType.PERSONAL),
                new PhoneEntity("+34", "676615106", PhoneType.COMPANY)
            ),
            PhoneType.COMPANY,
            "+34676615106"
        ),
        Arguments.of(
            List.of(
                new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)
            ),
            PhoneType.COMPANY,
            null
        )
    );
  }

  @ParameterizedTest
  @MethodSource("createPhoneParameters")
  @DisplayName("Create a phone object from a full phone number and phone type correctly")
  void createPhoneTest(String fullNumber, PhoneType phoneType, PhoneEntity expected) {
    PhoneEntity result = employeeInfrastructureMapper.createPhone(fullNumber, phoneType);
    Assertions.assertEquals(expected, result);
  }

  private static Stream<Arguments> createPhoneParameters() {
    return Stream.of(
        Arguments.of(
            "+34722748406",
            PhoneType.PERSONAL,
            new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)
        ),
        Arguments.of(
            "+44676615106",
            PhoneType.COMPANY,
            new PhoneEntity("+44", "676615106", PhoneType.COMPANY)
        )
    );
  }

}