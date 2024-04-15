package com.example.contract.employee.mapper;

import java.util.List;

import com.example.application.employee.cmd.dto.EmployeeCreateCmd;
import com.example.application.employee.cmd.dto.EmployeeDeleteCmd;
import com.example.application.employee.cmd.dto.EmployeeUpdateCmd;
import com.example.application.employee.query.dto.EmployeeByIdQuery;
import com.example.application.employee.query.dto.EmployeeByNameQuery;
import com.example.contract.employee.dto.EmployeeNameDTO;
import com.example.contract.employee.dto.EmployeeNifDTO;
import com.example.contract.employee.dto.EmployeeRequestDTO;
import com.example.contract.employee.dto.EmployeeResponseDTO;
import com.example.contract.employee.dto.PhoneDTO;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmployeeContractMapperImplTest {

  private final EmployeeContractMapper mapper = new EmployeeContractMapperImpl();

  @Test
  @DisplayName("Mapping EmployeeRequestDTO to EmployeeCreateCmd correctly")
  void mapToEmployeeCreateCmdTest() {
    EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender("Male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");
    EmployeeCreateCmd expected = new EmployeeCreateCmd()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender("Male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");

    EmployeeCreateCmd result = mapper.mapToEmployeeCreateCmd(employeeRequestDTO);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping id to EmployeeDeleteCmd correctly")
  void mapToEmployeeDeleteCmdTest() {
    EmployeeNifDTO employeeNifDTO = new EmployeeNifDTO("45134320V");
    EmployeeDeleteCmd expected = new EmployeeDeleteCmd(employeeNifDTO.getNif());
    EmployeeDeleteCmd result = mapper.mapToEmployeeDeleteCmd(employeeNifDTO);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping id and EmployeeRequestDTO to EmployeeUpdateCmd correctly")
  void mapToEmployeeUpdateCmdTest() {
    EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender("Male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");
    EmployeeUpdateCmd expected = new EmployeeUpdateCmd()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender("Male")
        .setPersonalPhone("+34722748406")
        .setEmail("wmlopes0@gmail.com");

    EmployeeUpdateCmd result = mapper.mapToEmployeeUpdateCmd(employeeRequestDTO);
    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping EmployeeNifDTO to EmployeeByIdQuery correctly")
  void mapToEmployeeByIdQueryTest() {
    EmployeeNifDTO employeeNifDTO = new EmployeeNifDTO("45134320V");
    EmployeeByIdQuery expected = new EmployeeByIdQuery(employeeNifDTO.getNif());
    EmployeeByIdQuery result = mapper.mapToEmployeeByIdQuery(employeeNifDTO);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping EmployeeNameDTO to EmployeeByNameQuery correctly")
  void mapToEmployeeByNameQueryTest() {
    EmployeeNameDTO employeeNameDTO = new EmployeeNameDTO("Walter");
    EmployeeByNameQuery expected = new EmployeeByNameQuery(employeeNameDTO.getName());
    EmployeeByNameQuery result = mapper.mapToEmployeeByNameQuery(employeeNameDTO);

    Assertions.assertEquals(expected, result);
  }

  @Test
  @DisplayName("Mapping Employee to EmployeeResponseDTO correctly")
  void mapToResponseDTOTest() {
    Employee employee = new Employee()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE)
        .setPersonalPhone("+34722748406")
        .setCompanyPhone("+34676615106")
        .setEmail("wmlopes0@gmail.com");

    EmployeeResponseDTO expected = new EmployeeResponseDTO()
        .setNif("45134320V")
        .setCompleteName("Martín Lopes, Walter")
        .setBirthYear(1998)
        .setAge(26)
        .setAdult(true)
        .setGender("Male")
        .setPhones(List.of(
            new PhoneDTO("+34722748406", PhoneType.PERSONAL.name()),
            new PhoneDTO("+34676615106", PhoneType.COMPANY.name())))
        .setEmail("wmlopes0@gmail.com");

    EmployeeResponseDTO result = mapper.mapToResponseDTO(employee);
    Assertions.assertEquals(expected, result);
  }
}