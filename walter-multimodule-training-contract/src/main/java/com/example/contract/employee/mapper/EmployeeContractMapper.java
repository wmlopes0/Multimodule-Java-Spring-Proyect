package com.example.contract.employee.mapper;

import java.util.ArrayList;
import java.util.List;

import com.example.application.employee.cmd.dto.AddEmployeeToCompanyCmd;
import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.cmd.dto.RemoveEmployeeFromCompanyCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.domain.entity.Employee;
import com.example.domain.entity.PhoneType;
import org.example.rest.model.CompanyDTO;
import org.example.rest.model.EmployeeRequestDTO;
import org.example.rest.model.EmployeeResponseDTO;
import org.example.rest.model.EmployeeUpdateDTO;
import org.example.rest.model.PhoneDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ON_IMPLICIT_CONVERSION)
public interface EmployeeContractMapper {

  EmployeeCreateCmd mapToEmployeeCreateCmd(EmployeeRequestDTO employeeRequestDTO);

  EmployeeDeleteCmd mapToEmployeeDeleteCmd(String nif);

  EmployeeUpdateCmd mapToEmployeeUpdateCmd(String nif, EmployeeUpdateDTO employeeUpdateDTO);

  EmployeeByIdQuery mapToEmployeeByIdQuery(String nif);

  EmployeeByNameQuery mapToEmployeeByNameQuery(String name);

  @Mapping(target = "gender", expression = "java(employee.getGender() != null ? employee.getGender().name() : null)")
  @Mapping(target = "phones", source = "employee", qualifiedByName = "mapPhones")
  @Mapping(target = "age", source = "birthYear", qualifiedByName = "calculateAge")
  @Mapping(target = "adult", source = "birthYear", qualifiedByName = "isAdult")
  @Mapping(target = "completeName", expression = "java(employee.getSurname() + \", \" + employee.getName())")
  EmployeeResponseDTO mapToResponseDTO(Employee employee);

  AddEmployeeToCompanyCmd mapToAddEmployeeToCompanyCmd(String nif, CompanyDTO companyDTO);

  RemoveEmployeeFromCompanyCmd removeEmployeeFromCompanyCmd(String nif, CompanyDTO companyDTO);

  @Named("mapPhones")
  default List<PhoneDTO> mapPhones(Employee employee) {
    List<PhoneDTO> phones = new ArrayList<>();

    if (employee.getPersonalPhone() != null && !employee.getPersonalPhone().isEmpty()) {
      PhoneDTO personalPhone = createPhone(employee.getPersonalPhone(), PhoneType.PERSONAL);
      if (personalPhone != null) {
        phones.add(personalPhone);
      }
    }

    if (employee.getCompanyPhone() != null && !employee.getCompanyPhone().isEmpty()) {
      PhoneDTO companyPhone = createPhone(employee.getCompanyPhone(), PhoneType.COMPANY);
      if (companyPhone != null) {
        phones.add(companyPhone);
      }
    }

    return phones;
  }

  default PhoneDTO createPhone(String fullNumber, PhoneType type) {
    if (fullNumber != null && !fullNumber.isEmpty()) {
      String prefix = fullNumber.substring(0, 3);
      String number = fullNumber.substring(3);
      return new PhoneDTO().setNumber(prefix + number).setType(type.name());
    }
    return null;
  }

  @Named("calculateAge")
  default Integer calculateAge(Integer birthYear) {
    if (birthYear == null) {
      return null;
    }
    int currentYear = java.time.Year.now().getValue();
    return currentYear - birthYear;
  }

  @Named("isAdult")
  default Boolean isAdult(Integer birthYear) {
    if (birthYear == null) {
      return null;
    }
    return calculateAge(birthYear) >= 18;
  }

}
