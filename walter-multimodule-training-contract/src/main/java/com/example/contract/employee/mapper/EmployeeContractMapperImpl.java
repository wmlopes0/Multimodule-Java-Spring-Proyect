package com.example.contract.employee.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.contract.employee.dto.employee.EmployeeRequestDTO;
import com.example.contract.employee.dto.employee.EmployeeResponseDTO;
import com.example.contract.employee.dto.employee.EmployeeUpdateDTO;
import com.example.contract.employee.dto.employee.PhoneDTO;
import com.example.domain.entity.Employee;
import com.example.domain.entity.PhoneType;
import org.springframework.stereotype.Component;

@Component
public class EmployeeContractMapperImpl implements EmployeeContractMapper {

  @Override
  public EmployeeCreateCmd mapToEmployeeCreateCmd(EmployeeRequestDTO employeeRequestDTO) {
    return new EmployeeCreateCmd()
        .setNif(employeeRequestDTO.getNif())
        .setName(employeeRequestDTO.getName())
        .setSurname(employeeRequestDTO.getSurname())
        .setBirthYear(employeeRequestDTO.getBirthYear())
        .setGender(employeeRequestDTO.getGender())
        .setPersonalPhone(employeeRequestDTO.getPersonalPhone())
        .setCompanyPhone(employeeRequestDTO.getCompanyPhone())
        .setEmail(employeeRequestDTO.getEmail());
  }

  @Override
  public EmployeeDeleteCmd mapToEmployeeDeleteCmd(String nif) {
    return new EmployeeDeleteCmd(nif);
  }

  @Override
  public EmployeeUpdateCmd mapToEmployeeUpdateCmd(String nif, EmployeeUpdateDTO employeeUpdateDTO) {
    return new EmployeeUpdateCmd()
        .setNif(nif)
        .setName(employeeUpdateDTO.getName())
        .setSurname(employeeUpdateDTO.getSurname())
        .setBirthYear(employeeUpdateDTO.getBirthYear())
        .setGender(employeeUpdateDTO.getGender())
        .setPersonalPhone(employeeUpdateDTO.getPersonalPhone())
        .setCompanyPhone(employeeUpdateDTO.getCompanyPhone())
        .setEmail(employeeUpdateDTO.getEmail());
  }

  @Override
  public EmployeeByIdQuery mapToEmployeeByIdQuery(String nif) {
    return new EmployeeByIdQuery(nif);
  }

  @Override
  public EmployeeByNameQuery mapToEmployeeByNameQuery(String name) {
    return new EmployeeByNameQuery(name);
  }

  @Override
  public EmployeeResponseDTO mapToResponseDTO(Employee employee) {
    int actualYear = LocalDate.now().getYear();
    int age = actualYear - employee.getBirthYear();
    boolean adult = age >= 18;
    String gender = employee.getGender().getCode() == 1 ? "Male" : "Female";

    List<PhoneDTO> phones = new ArrayList<>();
    if (employee.getPersonalPhone() != null) {
      phones.add(new PhoneDTO(employee.getPersonalPhone(), PhoneType.PERSONAL.name()));
    }
    if (employee.getCompanyPhone() != null) {
      phones.add(new PhoneDTO(employee.getCompanyPhone(), PhoneType.COMPANY.name()));
    }

    return new EmployeeResponseDTO()
        .setNif(employee.getNif())
        .setCompleteName(employee.getSurname() + ", " + employee.getName())
        .setBirthYear(employee.getBirthYear())
        .setAge(age)
        .setAdult(adult)
        .setGender(gender)
        .setPhones(phones)
        .setEmail(employee.getEmail());
  }
}