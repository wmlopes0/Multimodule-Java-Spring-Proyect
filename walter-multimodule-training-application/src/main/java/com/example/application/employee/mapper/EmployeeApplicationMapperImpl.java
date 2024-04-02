package com.example.application.employee.mapper;

//@Component
//public class EmployeeApplicationMapperImpl implements EmployeeApplicationMapper {

//  @Override
//  public EmployeeCreateCmd mapToEmployeeCreateCmd(EmployeeNameDTO employeeNameDTO) {
//    return new EmployeeCreateCmd(employeeNameDTO.getName());
//  }
//
//  @Override
//  public EmployeeUpdateCmd mapToEmployeeUpdateCmd(Long id, EmployeeNameDTO employeeNameDTO) {
//    return new EmployeeUpdateCmd(id, employeeNameDTO.getName());
//  }
//
//  @Override
//  public EmployeeNameDetailsDTO mapToDetailsDTO(Employee employee) {
//    return new EmployeeNameDetailsDTO(employee.getNumber(),
//        Optional.ofNullable(employee.getName())
//            .map(String::toUpperCase)
//            .orElse(null),
//        Optional.ofNullable(employee.getName())
//            .map(String::length)
//            .orElse(0));
//  }
//
//  @Override
//  public EmployeeResponseDTO mapToResponseDTO(Employee employee) {
//    return new EmployeeResponseDTO()
//        .setNumber(employee.getNumber())
//        .setName(employee.getName());
//  }
//}
