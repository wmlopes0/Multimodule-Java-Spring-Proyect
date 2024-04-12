package com.example.infrastructure.mapper;

import java.util.ArrayList;
import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeNifVO;
import com.example.domain.vo.EmployeeVO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.entity.Phone;
import com.example.infrastructure.entity.PhoneType;
import org.springframework.stereotype.Component;

@Component
public class EmployeeInfrastructureMapperImpl implements EmployeeInfrastructureMapper {

  @Override
  public Employee mapToDomain(EmployeeEntity employeeEntity) {
    return new Employee()
        .setNif(employeeEntity.getNif())
        .setName(employeeEntity.getName())
        .setSurname(employeeEntity.getLastName())
        .setBirthYear(employeeEntity.getBirthYear())
        .setGender(mapToGender(employeeEntity.getGender()))
        .setCompanyPhone(extractPhoneWithTypeOfList(employeeEntity.getPhones(), PhoneType.COMPANY))
        .setPersonalPhone(extractPhoneWithTypeOfList(employeeEntity.getPhones(), PhoneType.PERSONAL))
        .setEmail(employeeEntity.getEmail());
  }

  @Override
  public EmployeeEntity mapToEntity(EmployeeNameVO employeeNameVO) {
    return new EmployeeEntity()
        .setName(employeeNameVO.getName());
  }

  @Override
  public EmployeeEntity mapToEntity(EmployeeNifVO employeeNifVO) {
    return new EmployeeEntity()
        .setNif(employeeNifVO.getNif());
  }

  @Override
  public EmployeeEntity mapToEntity(EmployeeVO employeeVO) {
    List<Phone> phones = new ArrayList<>();

    Phone companyPhone = createPhone(employeeVO.getCompanyPhone(), PhoneType.COMPANY);
    if (companyPhone != null) {
      phones.add(companyPhone);
    }

    Phone personalPhone = createPhone(employeeVO.getPersonalPhone(), PhoneType.PERSONAL);
    if (personalPhone != null) {
      phones.add(personalPhone);
    }

    return new EmployeeEntity()
        .setNif(employeeVO.getNif())
        .setName(employeeVO.getName())
        .setLastName(employeeVO.getSurname())
        .setBirthYear(employeeVO.getBirthYear())
        .setGender(employeeVO.getGender().getCode())
        .setPhones(phones)
        .setEmail(employeeVO.getEmail());
  }

  @Override
  public Gender mapToGender(int genderCode) {
    switch (genderCode) {
      case 1:
        return Gender.MALE;
      case 2:
        return Gender.FEMALE;
      default:
        throw new IllegalArgumentException("Invalid gender code: " + genderCode);
    }
  }

  @Override
  public String extractPhoneWithTypeOfList(List<Phone> phones, PhoneType type) {
    for (Phone phone : phones) {
      if (phone.getType().equals(type)) {
        return phone.getNumber();
      }
    }
    return null;
  }

  @Override
  public Phone createPhone(String fullNumber, PhoneType type) {
    if (fullNumber != null && !fullNumber.isEmpty()) {
      String prefix = fullNumber.substring(0, 3);
      String number = fullNumber.substring(3);
      return new Phone(prefix, number, type);
    }
    return null;
  }

}
