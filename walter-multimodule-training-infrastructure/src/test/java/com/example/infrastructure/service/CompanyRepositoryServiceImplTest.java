package com.example.infrastructure.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.example.domain.entity.Company;
import com.example.domain.entity.Employee;
import com.example.domain.entity.Gender;
import com.example.domain.entity.PhoneType;
import com.example.domain.exception.CompanyNotFoundException;
import com.example.domain.service.EmployeeService;
import com.example.domain.vo.company.CompanyCreateVO;
import com.example.domain.vo.company.CompanyUpdateVO;
import com.example.domain.vo.employee.EmployeeNifVO;
import com.example.domain.vo.employee.EmployeeVO;
import com.example.infrastructure.entity.CompanyEntity;
import com.example.infrastructure.entity.EmployeeEntity;
import com.example.infrastructure.entity.PhoneEntity;
import com.example.infrastructure.mapper.company.CompanyInfrastructureMapper;
import com.example.infrastructure.mapper.employee.EmployeeInfrastructureMapper;
import com.example.infrastructure.repository.CompanyRepository;
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
class CompanyRepositoryServiceImplTest {

  @Mock
  private CompanyRepository repository;

  @Mock
  private EmployeeService employeeService;

  @Mock
  private CompanyInfrastructureMapper companyMapper;

  @Mock
  private EmployeeInfrastructureMapper employeeMapper;

  @InjectMocks
  private CompanyRepositoryServiceImpl service;

  @ParameterizedTest
  @MethodSource("listCompaniesParameters")
  @DisplayName("Retrieve all companies successfully")
  void getCompaniesTest(CompanyEntity companyEntity1, CompanyEntity companyEntity2, Company company1, Company company2,
      List<Employee> listEmployee1, List<Employee> listEmployee2) {
    Mockito.when(repository.findAll()).thenReturn(List.of(companyEntity1, companyEntity2));
    Mockito.when(companyMapper.mapToDomain(companyEntity1)).thenReturn(company1);
    Mockito.when(companyMapper.mapToDomain(companyEntity2)).thenReturn(company2);
    Mockito.when(employeeService.findEmployeesByCompanyId(company1.getCif())).thenReturn(listEmployee1);
    Mockito.when(employeeService.findEmployeesByCompanyId(company2.getCif())).thenReturn(listEmployee2);

    List<Company> result = service.getCompanies();
    List<Company> expected = List.of(company1, company2);

    Assertions.assertEquals(expected, result);
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(companyMapper, atLeastOnce()).mapToDomain(companyEntity1);
    Mockito.verify(employeeService, times(1)).findEmployeesByCompanyId(company1.getCif());
    Mockito.verify(employeeService, times(1)).findEmployeesByCompanyId(company2.getCif());
  }

  private static Stream<Arguments> listCompaniesParameters() {
    return Stream.of(
        Arguments.of(
            new CompanyEntity()
                .setCif("Q4947066I")
                .setName("Company1 S.L"),
            new CompanyEntity()
                .setCif("B86017472")
                .setName("Company2 S.L"),
            new Company()
                .setCif("Q4947066I")
                .setName("Company1 S.L")
                .setEmployees(List.of(
                    new Employee()
                        .setNif("45134320V")
                        .setName("Walter")
                        .setSurname("Martín Lopes")
                        .setBirthYear(1998)
                        .setGender(Gender.MALE)
                        .setCompanyPhone("+34676615106")
                        .setPersonalPhone("+34722748406")
                        .setCompany("Q4947066I")
                        .setEmail("wmlopes0@gmail.com"),
                    new Employee()
                        .setNif("45132337N")
                        .setName("Raquel")
                        .setSurname("Barbero Sánchez")
                        .setBirthYear(1996)
                        .setGender(Gender.FEMALE)
                        .setPersonalPhone("+34676615106")
                        .setCompany("Q4947066I")
                        .setEmail("raquelbarberosanchez90@gmail.com")
                )),
            new Company()
                .setCif("B86017472")
                .setName("Company2 S.L")
                .setEmployees(List.of(
                    new Employee()
                        .setNif("27748713H")
                        .setName("Manolo")
                        .setSurname("Martín Lopes")
                        .setBirthYear(1998)
                        .setGender(Gender.MALE)
                        .setCompanyPhone("+34676615106")
                        .setPersonalPhone("+34722748406")
                        .setCompany("B86017472")
                        .setEmail("manolo@gmail.com"),
                    new Employee()
                        .setNif("83765493E")
                        .setName("Maria")
                        .setSurname("Barbero Sánchez")
                        .setBirthYear(1996)
                        .setGender(Gender.FEMALE)
                        .setPersonalPhone("+34676615106")
                        .setCompany("B86017472")
                        .setEmail("maria@gmail.com")
                )),
            List.of(
                new Employee()
                    .setNif("45134320V")
                    .setName("Walter")
                    .setSurname("Martín Lopes")
                    .setBirthYear(1998)
                    .setGender(Gender.MALE)
                    .setCompanyPhone("+34676615106")
                    .setPersonalPhone("+34722748406")
                    .setCompany("Q4947066I")
                    .setEmail("wmlopes0@gmail.com"),
                new Employee()
                    .setNif("45132337N")
                    .setName("Raquel")
                    .setSurname("Barbero Sánchez")
                    .setBirthYear(1996)
                    .setGender(Gender.FEMALE)
                    .setPersonalPhone("+34676615106")
                    .setCompany("Q4947066I")
                    .setEmail("raquelbarberosanchez90@gmail.com")
            ),
            List.of(
                new Employee()
                    .setNif("27748713H")
                    .setName("Manolo")
                    .setSurname("Martín Lopes")
                    .setBirthYear(1998)
                    .setGender(Gender.MALE)
                    .setCompanyPhone("+34676615106")
                    .setPersonalPhone("+34722748406")
                    .setCompany("B86017472")
                    .setEmail("manolo@gmail.com"),
                new Employee()
                    .setNif("83765493E")
                    .setName("Maria")
                    .setSurname("Barbero Sánchez")
                    .setBirthYear(1996)
                    .setGender(Gender.FEMALE)
                    .setPersonalPhone("+34676615106")
                    .setCompany("B86017472")
                    .setEmail("maria@gmail.com")
            )
        )
    );
  }

  @Test
  @DisplayName("Retrieve empty list when no companies are available")
  void getCompaniesEmptyTest() {
    Mockito.when(repository.findAll()).thenReturn(List.of());

    List<Company> result = service.getCompanies();
    List<Company> expected = List.of();

    Assertions.assertEquals(expected, result);
    Mockito.verify(repository, times(1)).findAll();
    Mockito.verify(companyMapper, never()).mapToDomain(any(CompanyEntity.class));
  }

  @ParameterizedTest
  @MethodSource("getCompanyParameters")
  @DisplayName("Retrieve a company successfully")
  void getCompanyTest(String cif, CompanyEntity companyEntity, Company company, List<Employee> listEmployee) {
    Mockito.when(repository.findById(cif)).thenReturn(Optional.of(companyEntity));
    Mockito.when(companyMapper.mapToDomain(companyEntity)).thenReturn(company);
    Mockito.when(employeeService.findEmployeesByCompanyId(company.getCif())).thenReturn(listEmployee);

    Company result = service.getCompany(cif);

    Assertions.assertEquals(company, result);
    Mockito.verify(repository, times(1)).findById(cif);
    Mockito.verify(companyMapper, times(1)).mapToDomain(companyEntity);
    Mockito.verify(employeeService, times(1)).findEmployeesByCompanyId(company.getCif());
  }

  private static Stream<Arguments> getCompanyParameters() {
    return Stream.of(
        Arguments.of(
            "Q4947066I",
            new CompanyEntity()
                .setCif("Q4947066I")
                .setName("Company1 S.L"),
            new Company()
                .setCif("Q4947066I")
                .setName("Company1 S.L")
                .setEmployees(List.of(
                    new Employee()
                        .setNif("45134320V")
                        .setName("Walter")
                        .setSurname("Martín Lopes")
                        .setBirthYear(1998)
                        .setGender(Gender.MALE)
                        .setCompanyPhone("+34676615106")
                        .setPersonalPhone("+34722748406")
                        .setCompany("Q4947066I")
                        .setEmail("wmlopes0@gmail.com"),
                    new Employee()
                        .setNif("45132337N")
                        .setName("Raquel")
                        .setSurname("Barbero Sánchez")
                        .setBirthYear(1996)
                        .setGender(Gender.FEMALE)
                        .setPersonalPhone("+34676615106")
                        .setCompany("Q4947066I")
                        .setEmail("raquelbarberosanchez90@gmail.com")
                )),
            List.of(
                new Employee()
                    .setNif("45134320V")
                    .setName("Walter")
                    .setSurname("Martín Lopes")
                    .setBirthYear(1998)
                    .setGender(Gender.MALE)
                    .setCompanyPhone("+34676615106")
                    .setPersonalPhone("+34722748406")
                    .setCompany("Q4947066I")
                    .setEmail("wmlopes0@gmail.com"),
                new Employee()
                    .setNif("45132337N")
                    .setName("Raquel")
                    .setSurname("Barbero Sánchez")
                    .setBirthYear(1996)
                    .setGender(Gender.FEMALE)
                    .setPersonalPhone("+34676615106")
                    .setCompany("Q4947066I")
                    .setEmail("raquelbarberosanchez90@gmail.com")
            )
        )
    );
  }

  @Test
  @DisplayName("Return null when retrieving a non-existing company")
  void getCompanyNotFoundTest() {
    Mockito.when(repository.findById("Q4947066I")).thenReturn(Optional.empty());
    Company result = service.getCompany("Q4947066I");

    Assertions.assertNull(result);
    Mockito.verify(repository, times(1)).findById("Q4947066I");
    Mockito.verify(companyMapper, never()).mapToDomain(any(CompanyEntity.class));
  }

  @ParameterizedTest
  @MethodSource("createCompanyParameters")
  @DisplayName("Create a company successfully")
  void createCompanyTest(CompanyCreateVO companyCreateVO, CompanyEntity companyEntity, Company company) {
    Mockito.when(companyMapper.mapToEntity(companyCreateVO)).thenReturn(companyEntity);
    Mockito.when(repository.save(companyEntity)).thenReturn(companyEntity);
    Mockito.when(companyMapper.mapToDomain(companyEntity)).thenReturn(company);

    Company result = service.createCompany(companyCreateVO);
    Assertions.assertEquals(company, result);

    Mockito.verify(companyMapper, times(1)).mapToEntity(companyCreateVO);
    Mockito.verify(repository, times(1)).save(companyEntity);
    Mockito.verify(companyMapper, times(1)).mapToDomain(companyEntity);
  }

  private static Stream<Arguments> createCompanyParameters() {
    return Stream.of(
        Arguments.of(
            CompanyCreateVO.builder()
                .cif("Q4947066I")
                .name("Company1 S.L")
                .build(),
            new CompanyEntity()
                .setCif("Q4947066I")
                .setName("Company1 S.L"),
            new Company()
                .setCif("Q4947066I")
                .setName("Company1 S.L")
                .setEmployees(List.of())
        )
    );
  }

  @ParameterizedTest
  @MethodSource("updateCompanyParameters")
  @DisplayName("Update a company successfully")
  void updateCompanyTest(CompanyUpdateVO companyUpdateVO, CompanyEntity companyEntity, Company company) {
    Mockito.when(repository.findById(companyUpdateVO.getCif())).thenReturn(Optional.of(companyEntity));

    Mockito.when(repository.save(companyEntity)).thenReturn(companyEntity);
    Mockito.when(companyMapper.mapToDomain(companyEntity)).thenReturn(company);

    Company result = service.updateCompany(companyUpdateVO);

    Assertions.assertEquals(company, result);
    Mockito.verify(repository, Mockito.times(1)).findById(companyUpdateVO.getCif());
    Mockito.verify(repository, Mockito.times(1)).save(companyEntity);
    Mockito.verify(companyMapper, Mockito.times(1)).mapToDomain(companyEntity);
  }

  private static Stream<Arguments> updateCompanyParameters() {
    return Stream.of(
        Arguments.of(
            CompanyUpdateVO.builder()
                .cif("Q4947066I")
                .name("Company1 S.L")
                .build(),
            new CompanyEntity()
                .setCif("Q4947066I")
                .setName("Company1 S.L"),
            new Company()
                .setCif("Q4947066I")
                .setName("Company1 S.L")
                .setEmployees(List.of())
        )
    );
  }

  @ParameterizedTest
  @MethodSource("updateCompanyErrorParameters")
  @DisplayName("Throw exception on update company due to validation errors")
  void updateCompanyErrorTest(CompanyUpdateVO companyUpdateVO, CompanyEntity companyEntity) {
    Mockito.when(repository.findById(companyUpdateVO.getCif())).thenReturn(Optional.of(companyEntity));
    Mockito.when(repository.save(companyEntity)).thenThrow(new RuntimeException("An error occurred"));

    Assertions.assertThrows(RuntimeException.class, () -> service.updateCompany(companyUpdateVO));

    Mockito.verify(repository, Mockito.times(1)).findById(companyUpdateVO.getCif());
    Mockito.verify(repository, Mockito.times(1)).save(companyEntity);
    Mockito.verify(companyMapper, never()).mapToDomain(companyEntity);
  }

  private static Stream<Arguments> updateCompanyErrorParameters() {
    return Stream.of(
        Arguments.of(
            CompanyUpdateVO.builder()
                .cif("Q4947066I")
                .name("Company1 S.L")
                .build(),
            new CompanyEntity()
                .setCif("Q4947066I")
                .setName("Company1 S.L")
        )
    );
  }

  @Test
  @DisplayName("Fail to update a non-existing company")
  void updateCompanyNotFoundTest() {
    CompanyUpdateVO companyUpdateVO = CompanyUpdateVO.builder()
        .cif("Q4947066I")
        .name("Company1 S.L")
        .build();

    Mockito.when(repository.findById(companyUpdateVO.getCif())).thenReturn(Optional.empty());
    Assertions.assertThrows(CompanyNotFoundException.class, () -> service.updateCompany(companyUpdateVO));

    Mockito.verify(repository, Mockito.times(1)).findById(companyUpdateVO.getCif());
    Mockito.verify(repository, never()).save(any(CompanyEntity.class));
    Mockito.verify(companyMapper, never()).mapToDomain(any(CompanyEntity.class));
  }

  @Test
  @DisplayName("Delete a company successfully")
  void deleteCompanyTest() {
    String cif = "Q4947066I";
    Mockito.when(repository.existsById(cif)).thenReturn(true);

    Assertions.assertTrue(service.deleteCompany(cif));

    Mockito.verify(repository, Mockito.times(1)).existsById(cif);
    Mockito.verify(repository, Mockito.times(1)).deleteById(cif);
    Mockito.verify(employeeService, Mockito.times(1)).dissociateEmployeesFromCompany(cif);
  }

  @Test
  @DisplayName("Fail to delete a non-existing company")
  void deleteCompanyNotFoundTest() {
    String cif = "Q4947066I";
    Mockito.when(repository.existsById(cif)).thenReturn(false);

    Assertions.assertFalse(service.deleteCompany(cif));

    Mockito.verify(repository, Mockito.times(1)).existsById(cif);
    Mockito.verify(repository, never()).deleteById(cif);
    Mockito.verify(employeeService, never()).dissociateEmployeesFromCompany(cif);
  }

  @ParameterizedTest
  @MethodSource("addEmployeeToCompanyParameters")
  @DisplayName("Add an employee to a company successfully")
  void addEmployeeToCompanyTest(String nif, String cif, EmployeeNifVO employeeNifVO, Employee employee, EmployeeEntity employeeEntity,
      CompanyEntity companyEntity, Company company, List<Employee> listEmployees) {

    Mockito.when(employeeService.getEmployeeById(employeeNifVO)).thenReturn(employee);
    Mockito.when(repository.findById(cif)).thenReturn(Optional.of(companyEntity));

    EmployeeVO employeeUpdated = EmployeeVO.builder()
        .nif(nif)
        .company(cif)
        .build();
    employeeEntity.setCompany(cif);
    employee.setCompany(cif);

    Mockito.when(employeeService.updateEmployeeById(employeeUpdated)).thenReturn(employee);
    Mockito.when(employeeService.findEmployeesByCompanyId(cif)).thenReturn(listEmployees);
    Mockito.when(companyMapper.mapToDomain(companyEntity)).thenReturn(company);

    Company result = service.addEmployeeToCompany(nif, cif);

    Assertions.assertEquals(company, result);
    Mockito.verify(employeeService, Mockito.times(1)).getEmployeeById(employeeNifVO);
    Mockito.verify(repository, Mockito.times(1)).findById(cif);
    Mockito.verify(companyMapper, times(1)).mapToDomain(companyEntity);
    Mockito.verify(employeeService, Mockito.times(1)).updateEmployeeById(employeeUpdated);
    Mockito.verify(employeeService, Mockito.times(1)).findEmployeesByCompanyId(cif);
  }

  private static Stream<Arguments> addEmployeeToCompanyParameters() {
    return Stream.of(
        Arguments.of(
            "45134320V",
            "Q4947066I",
            EmployeeNifVO.builder().nif("45134320V").build(),
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setCompanyPhone("+34676615106")
                .setPersonalPhone("+34722748406")
                .setCompany(null)
                .setEmail("wmlopes0@gmail.com"),
            new EmployeeEntity()
                .setNif("45134320V")
                .setName("Walter")
                .setLastName("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE.getCode())
                .setPhones(List.of(
                    new PhoneEntity("+34", "676615106", PhoneType.COMPANY),
                    new PhoneEntity("+34", "722748406", PhoneType.PERSONAL)))
                .setCompany(null)
                .setEmail("wmlopes0@gmail.com"),
            new CompanyEntity()
                .setCif("Q4947066I")
                .setName("Company1 S.L"),
            new Company()
                .setCif("Q4947066I")
                .setName("Company1 S.L")
                .setEmployees(List.of(
                    new Employee()
                        .setNif("45134320V")
                        .setName("Walter")
                        .setSurname("Martín Lopes")
                        .setBirthYear(1998)
                        .setGender(Gender.MALE)
                        .setCompanyPhone("+34676615106")
                        .setPersonalPhone("+34722748406")
                        .setCompany("Q4947066I")
                        .setEmail("wmlopes0@gmail.com"))
                ),
            List.of(
                new Employee()
                    .setNif("45134320V")
                    .setName("Walter")
                    .setSurname("Martín Lopes")
                    .setBirthYear(1998)
                    .setGender(Gender.MALE)
                    .setCompanyPhone("+34676615106")
                    .setPersonalPhone("+34722748406")
                    .setCompany("Q4947066I")
                    .setEmail("wmlopes0@gmail.com")))
    );
  }

  @Test
  @DisplayName("Fail to add a non-existing employee to a company")
  void addEmployeeNotFoundToCompanyTest() {
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder().nif("45134320V").build();
    Mockito.when(employeeService.getEmployeeById(employeeNifVO)).thenReturn(null);

    Company result = service.addEmployeeToCompany("45134320V", "Q4947066I");
    Assertions.assertNull(result);
    Mockito.verify(employeeService, Mockito.times(1)).getEmployeeById(employeeNifVO);
    Mockito.verify(employeeMapper, never()).mapDomainToEntity(any(Employee.class));
    Mockito.verify(repository, never()).findById(any(String.class));
    Mockito.verify(employeeService, never()).updateEmployeeById(any(EmployeeVO.class));
    Mockito.verify(repository, never()).save(any(CompanyEntity.class));
    Mockito.verify(companyMapper, never()).mapToDomain(any(CompanyEntity.class));
  }

  @Test
  @DisplayName("Fail to add an employee to a non-existing company")
  void addEmployeeToCompanyNotFoundTest() {
    String nif = "45134320V";
    String cif = "Q4947066I";
    Employee employee = new Employee()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE)
        .setCompanyPhone("+34676615106")
        .setPersonalPhone("+34722748406")
        .setCompany(null)
        .setEmail("wmlopes0@gmail.com");

    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder().nif("45134320V").build();
    Mockito.when(employeeService.getEmployeeById(employeeNifVO)).thenReturn(employee);
    Mockito.when(repository.findById(cif)).thenReturn(Optional.empty());

    Company result = service.addEmployeeToCompany(nif, cif);
    Assertions.assertNull(result);
    Mockito.verify(employeeService, Mockito.times(1)).getEmployeeById(employeeNifVO);
    Mockito.verify(repository, times(1)).findById(cif);
    Mockito.verify(employeeMapper, never()).mapDomainToEntity(any(Employee.class));
    Mockito.verify(employeeService, never()).updateEmployeeById(any(EmployeeVO.class));
    Mockito.verify(repository, never()).save(any(CompanyEntity.class));
    Mockito.verify(companyMapper, never()).mapToDomain(any(CompanyEntity.class));
  }

  @ParameterizedTest
  @MethodSource("removeEmployeeFromCompanyParameters")
  @DisplayName("Remove an employee from a company successfully")
  void removeEmployeeFromCompanyTest(String nif, String cif, Employee employee, Employee employeeChanged, CompanyEntity companyEntity) {
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder().nif(nif).build();
    EmployeeVO employeeUpdate = EmployeeVO.builder()
        .nif(nif)
        .company(null)
        .build();

    Mockito.when(employeeService.getEmployeeById(employeeNifVO)).thenReturn(employee);
    Mockito.when(repository.findById(cif)).thenReturn(Optional.of(companyEntity));
    Mockito.when(employeeService.removeCompanyFromEmployee(employeeUpdate)).thenReturn(employeeChanged);

    boolean result = service.removeEmployeeFromCompany(nif, cif);
    Assertions.assertTrue(result);

    Mockito.verify(employeeService, Mockito.times(1)).getEmployeeById(employeeNifVO);
    Mockito.verify(repository, Mockito.times(1)).findById(cif);
    Mockito.verify(employeeService, Mockito.times(1)).removeCompanyFromEmployee(employeeUpdate);
  }

  private static Stream<Arguments> removeEmployeeFromCompanyParameters() {
    return Stream.of(
        Arguments.of(
            "45134320V",
            "Q4947066I",
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setCompanyPhone("+34676615106")
                .setPersonalPhone("+34722748406")
                .setCompany("Q4947066I")
                .setEmail("wmlopes0@gmail.com"),
            new Employee()
                .setNif("45134320V")
                .setName("Walter")
                .setSurname("Martín Lopes")
                .setBirthYear(1998)
                .setGender(Gender.MALE)
                .setCompanyPhone("+34676615106")
                .setPersonalPhone("+34722748406")
                .setCompany(null)
                .setEmail("wmlopes0@gmail.com"),
            new CompanyEntity()
                .setCif("Q4947066I")
                .setName("Company1 S.L")
        ));
  }

  @Test
  @DisplayName("Fail to remove a non-existing employee from a company")
  void removeEmployeeNotFoundFromCompanyTest() {
    String nif = "45134320V";
    String cif = "Q4947066I";
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder().nif(nif).build();

    Mockito.when(employeeService.getEmployeeById(employeeNifVO)).thenReturn(null);

    boolean result = service.removeEmployeeFromCompany(nif, cif);
    Assertions.assertFalse(result);

    Mockito.verify(employeeService, Mockito.times(1)).getEmployeeById(employeeNifVO);
    Mockito.verify(repository, Mockito.never()).findById(cif);
    Mockito.verify(repository, Mockito.never()).save(any(CompanyEntity.class));
  }

  @Test
  @DisplayName("Fail to remove an employee from a non-existing company")
  void removeEmployeeFromCompanyNotFoundTest() {
    String nif = "45134320V";
    String cif = "Q4947066I";
    Employee employee = new Employee()
        .setNif("45134320V")
        .setName("Walter")
        .setSurname("Martín Lopes")
        .setBirthYear(1998)
        .setGender(Gender.MALE)
        .setCompanyPhone("+34676615106")
        .setPersonalPhone("+34722748406")
        .setCompany("Q4947066I")
        .setEmail("wmlopes0@gmail.com");
    EmployeeNifVO employeeNifVO = EmployeeNifVO.builder().nif(nif).build();

    Mockito.when(employeeService.getEmployeeById(employeeNifVO)).thenReturn(employee);
    Mockito.when(repository.findById(cif)).thenReturn(Optional.empty());

    boolean result = service.removeEmployeeFromCompany(nif, cif);
    Assertions.assertFalse(result);

    Mockito.verify(employeeService, Mockito.times(1)).getEmployeeById(employeeNifVO);
    Mockito.verify(repository, Mockito.times(1)).findById(cif);
    Mockito.verify(repository, Mockito.never()).save(any(CompanyEntity.class));
  }
}