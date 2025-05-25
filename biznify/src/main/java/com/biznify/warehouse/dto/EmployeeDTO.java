package com.biznify.warehouse.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private String name;
    private String email;
    private String phone;
    private String designation;
    private String department;
    private LocalDate dateOfJoining;
}
