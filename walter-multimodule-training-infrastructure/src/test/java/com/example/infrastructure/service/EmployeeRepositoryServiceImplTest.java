package com.example.infrastructure.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
import com.example.domain.exception.EmployeeNotFoundException;
import com.example.domain.vo.employee.EmployeeNameVO;
import com.example.domain.vo.employee.EmployeeNifVO;
import com.example.domain.vo.employee.EmployeeVO;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.entity.PhoneEntity;
import com.example.infrastructure.mapper.employee.EmployeeInfrastructureMapper;
import com.example.infrastructure.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeRepositoryServiceImplTest {

  @Mock
  private EmployeeRepository repository;

  @Mock
  private EmployeeInfrastructureMapper employeeInfrastructureMapper;

  @InjectMocks
  private EmployeeRepositoryServiceImpl service;

  @ParameterizedTest
  @MethodSource("listEmployeesParameters")
  @DisplayName("Retrieve Employee list successfully")
  void listEmployeesTest(EmployeeEntity employeeEntity1, EmployeeEntity employeeEntity2, Employee employee1, Employee employee2) {
    Mockito.when(repository.findAll()).thenReturn(List.of(employeeEntity1, employeeEntity2));
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity1)).thenReturn(employee1);
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity2)).thenReturn(employee2);

    List<Employee> result = service.listEmployees();
    List<Employee> expected = List.of(employee1, employee2);

    Assertions.assertEquals(expected, result);
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(employeeInfrastructureMapper, atLeastOnce()).mapToDomain(any(EmployeeEntity.class));
  }

  @ParameterizedTest
  @MethodSource("listEmployeesParameters")
  @DisplayName("Retrieve and map employees by company ID successfully")
  void findEmployeesByCompanyId(EmployeeEntity employeeEntity1, EmployeeEntity employeeEntity2, Employee employee1, Employee employee2) {
    String cif = "Q4947066I";
    Mockito.when(repository.findByCompany(cif)).thenReturn(List.of(employeeEntity1, employeeEntity2));
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity1)).thenReturn(employee1);
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity2)).thenReturn(employee2);

    List<Employee> result = service.findEmployeesByCompanyId(cif);
    List<Employee> expected = List.of(employee1, employee2);

    Assertions.assertEquals(expected, result);
    Mockito.verify(repository, times(1)).findByCompany(cif);
    Mockito.verify(employeeInfrastructureMapper, atLeastOnce()).mapToDomain(any(EmployeeEntity.class));
  }

  private static Stream<Arguments> listEmployeesParameters() {
    return Stream.of(
        Arguments.of(
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setCompany("Q4947066I")
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeEntity()
                .setNif("45132337N")
                .setName("Raquel")
                .setBirthYear(1996)
                .setGender(Gender.FEMALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "676615106", PhoneType.PERSONAL)))
                .setCompany("Q4947066I")
                .setEmail("raquelbarberosanchez90@gmail.com"),
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setPersonalPhone("+34722748406")
                .setCompany("Q4947066I")
                .setEmail("wmlopes0@gmail.com"),
            new Employee()
                .setNif("45132337N")
                .setName("Raquel")
                .setBirthYear(1996)
                .setGender(Gender.FEMALE)
                .setPersonalPhone("+34676615106")
                .setCompany("Q4947066I")
                .setEmail("raquelbarberosanchez90@gmail.com")
        )
    );
  }

  @Test
  @DisplayName("Retrieve empty Employee list when no employees present")
  void emptyListEmployeesTest() {
    Mockito.when(repository.findAll()).thenReturn(List.of());

    List<Employee> result = service.listEmployees();
    List<Employee> expected = List.of();

    Assertions.assertEquals(expected, result);
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("ListEmployees Throws RuntimeException on Error")
  void listEmployeesErrorTest() {
    Mockito.when(repository.findAll()).thenThrow(new RuntimeException("An error occurred"));
    Assertions.assertThrows(RuntimeException.class, () -> service.listEmployees());
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @ParameterizedTest
  @MethodSource("getEmployeeByIdParameters")
  @DisplayName("Retrieve Employee by ID successfully")
  void getEmployeeByIdTest(EmployeeNifVO employeeNifVO, EmployeeEntity employeeEntity, Employee employee) {
    Mockito.when(repository.findById(employeeNifVO.getNif())).thenReturn(Optional.of(employeeEntity));
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.getEmployeeById(employeeNifVO);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).findById(employeeNifVO.getNif());
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToDomain(employeeEntity);
  }

  private static Stream<Arguments> getEmployeeByIdParameters() {
    return Stream.of(
        Arguments.of(
            EmployeeNifVO.builder().nif("45134320V").build(),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @Test
  @DisplayName("Employee Not Found by ID")
  void getEmployeeByIdNotFoundTest() {
    EmployeeNifVO employee = EmployeeNifVO.builder().nif("45134320V").build();

    Mockito.when(repository.findById(employee.getNif())).thenReturn(Optional.empty());
    Employee result = service.getEmployeeById(employee);

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).findById(employee.getNif());
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("GetEmployeeById Throws RuntimeException on Error")
  void getEmployeeByIdErrorTest() {
    EmployeeNifVO employee = EmployeeNifVO.builder().nif("45134320V").build();
    Mockito.when(repository.findById(employee.getNif())).thenThrow(new RuntimeException("An error occurred"));
    Assertions.assertThrows(RuntimeException.class, () -> service.getEmployeeById(employee));
    Mockito.verify(repository, times(1)).findById(employee.getNif());
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @ParameterizedTest
  @MethodSource("getEmployeeByNameParameters")
  @DisplayName("Retrieve Employee by name successfully")
  void getEmployeeByNameTest(EmployeeNameVO employeeNameVO, EmployeeEntity employeeEntity, Employee employee) {
    Mockito.when(repository.findFirstByNameContainingIgnoreCase(employeeNameVO.getName())).thenReturn(Optional.of(employeeEntity));
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.getEmployeeByName(employeeNameVO);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(employeeNameVO.getName());
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToDomain(employeeEntity);
  }

  private static Stream<Arguments> getEmployeeByNameParameters() {
    return Stream.of(
        Arguments.of(
            EmployeeNameVO.builder().name("Wal").build(),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @Test
  @DisplayName("Employee Not Found by name")
  void getEmployeeByNameNotFoundTest() {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();

    Mockito.when(repository.findFirstByNameContainingIgnoreCase(employeeNameVO.getName())).thenReturn(Optional.empty());
    Employee result = service.getEmployeeByName(employeeNameVO);

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(employeeNameVO.getName());
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @Test
  @DisplayName("GetEmployeeByName Throws RuntimeException on Error")
  void getEmployeeByNameErrorTest() {
    EmployeeNameVO employeeNameVO = EmployeeNameVO.builder().name("Walter").build();

    Mockito.when(repository.findFirstByNameContainingIgnoreCase(employeeNameVO.getName()))
        .thenThrow(new RuntimeException("An error occurred"));
    Assertions.assertThrows(RuntimeException.class, () -> service.getEmployeeByName(employeeNameVO));
    Mockito.verify(repository, times(1)).findFirstByNameContainingIgnoreCase(employeeNameVO.getName());
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  @ParameterizedTest
  @MethodSource("addUpdateEmployeeParameters")
  @DisplayName("Add new Employee successfully")
  void addEmployeeTest(EmployeeVO employeeVO, EmployeeEntity employeeEntity, Employee employee) {
    Mockito.when(employeeInfrastructureMapper.mapToEntity(employeeVO)).thenReturn(employeeEntity);
    Mockito.when(repository.save(employeeEntity)).thenReturn(employeeEntity);
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity)).thenReturn(employee);
    Employee result = service.addEmployee(employeeVO);

    Assertions.assertEquals(employee, result);
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToEntity(employeeVO);
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToDomain(employeeEntity);
  }

  @ParameterizedTest
  @MethodSource("addUpdateErrorEmployeeParameters")
  @DisplayName("AddEmployee Throws RuntimeException on Error")
  void addEmployeeErrorTest(EmployeeVO employeeVO, EmployeeEntity employeeEntity) {
    Mockito.when(employeeInfrastructureMapper.mapToEntity(employeeVO)).thenReturn(employeeEntity);
    Mockito.when(repository.save(employeeEntity)).thenThrow(new RuntimeException("An error occurred"));

    Assertions.assertThrows(RuntimeException.class, () -> service.addEmployee(employeeVO));
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToEntity(employeeVO);
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(employeeEntity);
  }

  @ParameterizedTest
  @MethodSource("addUpdateEmployeeParameters")
  @DisplayName("Update Employee information successfully")
  void updateEmployeeByIdTest(EmployeeVO employeeVO, EmployeeEntity employeeEntity, Employee employee) {
    Mockito.when(repository.findById(employeeVO.getNif())).thenReturn(Optional.of(employeeEntity));

    Mockito.when(repository.save(employeeEntity)).thenReturn(employeeEntity);
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity)).thenReturn(employee);

    Employee result = service.updateEmployeeById(employeeVO);

    Assertions.assertEquals(employee, result);
    Mockito.verify(repository, times(1)).findById(employeeVO.getNif());
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToDomain(employeeEntity);
  }

  @ParameterizedTest
  @MethodSource("addUpdateErrorEmployeeParameters")
  @DisplayName("UpdateEmployee Throws RuntimeException on Error")
  void updateEmployeeErrorTest(EmployeeVO employeeVO, EmployeeEntity employeeEntity) {

    Mockito.when(repository.findById(employeeVO.getNif())).thenReturn(Optional.of(employeeEntity));
    Mockito.when(repository.save(employeeEntity)).thenThrow(new RuntimeException("An error occurred"));

    Assertions.assertThrows(RuntimeException.class, () -> service.updateEmployeeById(employeeVO));

    Mockito.verify(repository, times(1)).findById(employeeVO.getNif());
    Mockito.verify(repository, times(1)).save(employeeEntity);
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(employeeEntity);
  }

  @Test
  @DisplayName("Fail to update non-existent Employee")
  void updateEmployeeByIdNotFoundTest() {
    EmployeeVO employeeVO = EmployeeVO.builder()
        .nif("45134320V")
        .name("Walter")
        .birthYear(1998)
        .gender(Gender.MALE)
        .personalPhone("+34722748406")
        .email("wmlopes0@gmail.com")
        .build();

    Mockito.when(repository.findById(employeeVO.getNif())).thenReturn(Optional.empty());
    Assertions.assertThrows(EmployeeNotFoundException.class, () -> service.updateEmployeeById(employeeVO));

    Mockito.verify(repository, times(1)).findById(employeeVO.getNif());
    Mockito.verify(repository, never()).save(any(EmployeeEntity.class));
    Mockito.verify(employeeInfrastructureMapper, never()).mapToDomain(any(EmployeeEntity.class));
  }

  private static Stream<Arguments> addUpdateEmployeeParameters() {
    return Stream.of(
        Arguments.of(
            EmployeeVO.builder()
                .nif("45134320V")
                .name("Walter")
                .birthYear(1998)
                .gender(Gender.MALE)
                .personalPhone("+34722748406")
                .email("wmlopes0@gmail.com")
                .build(),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  private static Stream<Arguments> addUpdateErrorEmployeeParameters() {
    return Stream.of(
        Arguments.of(
            EmployeeVO.builder()
                .nif("45134320V")
                .name("Walter")
                .birthYear(1998)
                .gender(Gender.MALE)
                .personalPhone("+34722748406")
                .email("wmlopes0@gmail.com")
                .build(),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com")
        )
    );
  }

  @Test
  @DisplayName("Delete Employee by ID successfully")
  void deleteEmployeeByIdTest() {
    EmployeeNifVO employee = EmployeeNifVO.builder().nif("45134320V").build();

    Mockito.when(repository.existsById(employee.getNif())).thenReturn(true);

    boolean result = service.deleteEmployeeById(employee);

    Assertions.assertTrue(result);
    Mockito.verify(repository, times(1)).existsById(employee.getNif());
    Mockito.verify(repository, times(1)).deleteById(employee.getNif());
  }

  @Test
  @DisplayName("DeleteEmployeeById Throws RuntimeException on Error")
  void deleteEmployeeByIdErrorTest() {
    EmployeeNifVO employee = EmployeeNifVO.builder().nif("45134320V").build();

    Mockito.when(repository.existsById(employee.getNif())).thenReturn(true);
    Mockito.doThrow(new RuntimeException("An error occurred")).when(repository).deleteById(employee.getNif());

    Assertions.assertThrows(RuntimeException.class, () -> service.deleteEmployeeById(employee));

    Mockito.verify(repository, times(1)).existsById(employee.getNif());
    Mockito.verify(repository, times(1)).deleteById(employee.getNif());
  }

  @Test
  @DisplayName("Employee deletion fails for non-existent ID")
  void deleteEmployeeByIdNotFoundTest() {
    EmployeeNifVO employee = EmployeeNifVO.builder().nif("45134320V").build();

    Mockito.when(repository.existsById(employee.getNif())).thenReturn(false);

    boolean result = service.deleteEmployeeById(employee);

    Assertions.assertFalse(result);
    Mockito.verify(repository, times(1)).existsById(employee.getNif());
    Mockito.verify(repository, never()).deleteById(any(String.class));
  }

  @ParameterizedTest
  @MethodSource("removeCompanyFromEmployeeParameters")
  @DisplayName("Remove Company from Employee correctly")
  void removeCompanyFromEmployeeTest(String nif, EmployeeVO employeeVO, Optional<EmployeeEntity> existingEmployee,
      EmployeeEntity employeeEntity, Employee employee) {

    Mockito.when(repository.findById(nif)).thenReturn(existingEmployee);
    Mockito.when(repository.save(existingEmployee.get())).thenReturn(employeeEntity);
    Mockito.when(employeeInfrastructureMapper.mapToDomain(employeeEntity)).thenReturn(employee);

    Employee result = service.removeCompanyFromEmployee(employeeVO);
    Assertions.assertEquals(employee, result);

    Mockito.verify(repository, times(1)).findById(nif);
    Mockito.verify(repository, times(1)).save(existingEmployee.get());
    Mockito.verify(employeeInfrastructureMapper, times(1)).mapToDomain(employeeEntity);
  }

  private static Stream<Arguments> removeCompanyFromEmployeeParameters() {
    return Stream.of(
        Arguments.of(
            "45134320V",
            EmployeeVO.builder().nif("45134320V").company(null).build(),
            Optional.of(
                new EmployeeEntity()
                    .setNif("45134320V")
                    .setName("Walter")
                    .setLastName("Martín Lopes")
                    .setBirthYear(1998)
                    .setGender(Gender.MALE.getCode())
                    .setCompany("B86017472")
                    .setPhones(List.of(
                        new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                    .setEmail("wmlopes0@gmail.com")),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setCompany(null)
                .setPhones(List.of(
                    new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setEmail("wmlopes0@gmail.com"),
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setCompany(null)
                .setPersonalPhone("+34722748406")
                .setEmail("wmlopes0@gmail.com"))
    );
  }

}