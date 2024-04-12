package com.example.infrastructure.mapper;

import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.vo.EmployeeNameVO;
import com.example.domain.vo.EmployeeNifVO;
import com.example.domain.vo.EmployeeVO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.entity.Phone;
import com.example.infrastructure.entity.PhoneType;

public interface EmployeeInfrastructureMapper {

  Employee mapToDomain(EmployeeEntity employeeEntity);

  EmployeeEntity mapToEntity(EmployeeNameVO employeeNameVO);

  EmployeeEntity mapToEntity(EmployeeNifVO employeeNifVO);

  EmployeeEntity mapToEntity(EmployeeVO employeeVO);

  Gender mapToGender(int genderCode);

  String extractPhoneWithTypeOfList(List<Phone> phones, PhoneType type);

  Phone createPhone(String fullNumber, PhoneType type);
}
