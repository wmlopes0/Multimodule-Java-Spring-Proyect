package com.example.model;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EmployeeNameDetailsDTO {

    private Long number;
    private String name;
    private int nameLength;

}
