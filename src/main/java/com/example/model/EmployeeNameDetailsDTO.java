package com.example.model;

import lombok.Getter;

@Getter
public class EmployeeNameDetailsDTO {

    private Long number;
    private String name;
    private int nameLength;

    public EmployeeNameDetailsDTO(Employee employee) {
        this.number = employee.getNumber();
        this.name = employee.getName().toUpperCase();
        this.nameLength = name.length();
    }
}
