package com.example.infrastructure.mapper;

import java.util.List;

import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
import com.example.domain.vo.employee.EmployeeNameVO;
import com.example.domain.vo.employee.EmployeeNifVO;
import com.example.domain.vo.employee.EmployeeVO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.entity.PhoneEntity;

public interface EmployeeInfrastructureMapper {

  Employee mapToDomain(EmployeeEntity employeeEntity);

  EmployeeEntity mapToEntity(EmployeeNameVO employeeNameVO);

  EmployeeEntity mapToEntity(EmployeeNifVO employeeNifVO);

  EmployeeEntity mapToEntity(EmployeeVO employeeVO);

  Gender mapToGender(int genderCode);

  String extractPhoneWithTypeOfList(List<PhoneEntity> phones, PhoneType type);

  PhoneEntity createPhone(String fullNumber, PhoneType type);
}
