package com.example.infrastructure.mapper.employee;

import java.util.ArrayList;
import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
import com.example.domain.vo.employee.EmployeeNameVO;
import com.example.domain.vo.employee.EmployeeNifVO;
import com.example.domain.vo.employee.EmployeeVO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.entity.PhoneEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", imports = {PhoneType.class}, nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface EmployeeInfrastructureMapper {

  @Mapping(target = "gender", source = "gender", qualifiedByName = "genderName")
  @Mapping(target = "surname", source = "lastName")
  @Mapping(target = "companyPhone", expression = "java(extractPhoneWithTypeOfList(employeeEntity.getPhones(), PhoneType.COMPANY))")
  @Mapping(target = "personalPhone", expression = "java(extractPhoneWithTypeOfList(employeeEntity.getPhones(), PhoneType.PERSONAL))")
  Employee mapToDomain(EmployeeEntity employeeEntity);

  EmployeeEntity mapToEntity(EmployeeNameVO employeeNameVO);

  EmployeeEntity mapToEntity(EmployeeNifVO employeeNifVO);

  @Mapping(target = "lastName", source = "surname")
  @Mapping(target = "gender", expression = "java(employeeVO.getGender().getCode())")
  @Mapping(target = "phones", expression = "java(createPhones(employeeVO))")
  EmployeeEntity mapToEntity(EmployeeVO employeeVO);

  @Mapping(target = "lastName", source = "surname")
  @Mapping(target = "gender", expression = "java(employee.getGender().getCode())")
  @Mapping(target = "phones", expression = "java(createPhones(employee))")
  EmployeeEntity mapDomainToEntity(Employee employee);

  @Named("genderName")
  default Gender mapToGender(int genderCode) {
    return switch (genderCode) {
      case 1 -> Gender.MALE;
      case 2 -> Gender.FEMALE;
      default -> throw new IllegalArgumentException("Invalid gender code: " + genderCode);
    };
  }

  default String extractPhoneWithTypeOfList(List<PhoneEntity> phones, PhoneType type) {
    for (PhoneEntity phone : phones) {
      if (phone.getType().equals(type)) {
        return phone.getPrefix() + phone.getNumber();
      }
    }
    return null;
  }

  default List<PhoneEntity> createPhones(EmployeeVO employeeVO) {
    List<PhoneEntity> phones = new ArrayList<>();

    PhoneEntity companyPhone = createPhone(employeeVO.getCompanyPhone(), PhoneType.COMPANY);
    if (companyPhone != null) {
      phones.add(companyPhone);
    }

    PhoneEntity personalPhone = createPhone(employeeVO.getPersonalPhone(), PhoneType.PERSONAL);
    if (personalPhone != null) {
      phones.add(personalPhone);
    }

    return phones;
  }

  default List<PhoneEntity> createPhones(Employee employee) {
    List<PhoneEntity> phones = new ArrayList<>();

    PhoneEntity personalPhone = createPhone(employee.getPersonalPhone(), PhoneType.PERSONAL);
    if (personalPhone != null) {
      phones.add(personalPhone);
    }
    PhoneEntity companyPhone = createPhone(employee.getCompanyPhone(), PhoneType.COMPANY);
    if (companyPhone != null) {
      phones.add(companyPhone);
    }

    return phones;
  }

  default PhoneEntity createPhone(String fullNumber, PhoneType type) {
    if (fullNumber != null && !fullNumber.isEmpty()) {
      String prefix = fullNumber.substring(0, 3);
      String number = fullNumber.substring(3);
      return new PhoneEntity(prefix, number, type);
    }
    return null;
  }
}
