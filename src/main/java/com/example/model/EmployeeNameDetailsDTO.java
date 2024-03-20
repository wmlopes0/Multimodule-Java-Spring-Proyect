package com.example.model;

import lombok.Getter;

@Getter
public class EmployeeNameDetailsDTO {

    private final Long number;
    private final String name;
    private final int nameLength;

    public EmployeeNameDetailsDTO(Employee employee) {
        this.number = employee.getNumber();
        this.name = employee.getName().toUpperCase();
        this.nameLength = name.length();
    }
}
