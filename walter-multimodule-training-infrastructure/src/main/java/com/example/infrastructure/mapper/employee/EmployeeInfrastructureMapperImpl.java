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
    List<PhoneEntity> phones = new ArrayList<>();

    PhoneEntity companyPhone = createPhone(employeeVO.getCompanyPhone(), PhoneType.COMPANY);
    if (companyPhone != null) {
      phones.add(companyPhone);
    }

    PhoneEntity personalPhone = createPhone(employeeVO.getPersonalPhone(), PhoneType.PERSONAL);
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
  public EmployeeEntity mapDomainToEntity(Employee employee) {
    List<PhoneEntity> phones = new ArrayList<>();
    PhoneEntity personalPhone = createPhone(employee.getPersonalPhone(), PhoneType.PERSONAL);
    if (personalPhone != null) {
      phones.add(personalPhone);
    }

    PhoneEntity companyPhone = createPhone(employee.getCompanyPhone(), PhoneType.COMPANY);
    if (companyPhone != null) {
      phones.add(companyPhone);
    }

    return new EmployeeEntity()
        .setNif(employee.getNif())
        .setName(employee.getName())
        .setLastName(employee.getSurname())
        .setBirthYear(employee.getBirthYear())
        .setGender(employee.getGender().getCode())
        .setPhones(phones)
        .setEmail(employee.getEmail());
  }

  @Override
  public Gender mapToGender(int genderCode) {
    return switch (genderCode) {
      case 1 -> Gender.MALE;
      case 2 -> Gender.FEMALE;
      default -> throw new IllegalArgumentException("Invalid gender code: " + genderCode);
    };
  }

  @Override
  public String extractPhoneWithTypeOfList(List<PhoneEntity> phones, PhoneType type) {
    for (PhoneEntity phone : phones) {
      if (phone.getType().equals(type)) {
        return phone.getPrefix() + phone.getNumber();
      }
    }
    return null;
  }

  @Override
  public PhoneEntity createPhone(String fullNumber, PhoneType type) {
    if (fullNumber != null && !fullNumber.isEmpty()) {
      String prefix = fullNumber.substring(0, 3);
      String number = fullNumber.substring(3);
      return new PhoneEntity(prefix, number, type);
    }
    return null;
  }

}
