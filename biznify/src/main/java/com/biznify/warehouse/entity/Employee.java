package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import com.biznify.warehouse.common.Auditable;

@Data
@Entity
public class Employee extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Employeeid;

    private String name;
    private String email;
    private String phone;
    private String designation;
    private String department;
    private LocalDate dateOfJoining;
    
    @OneToMany(mappedBy = "manager")  // One manager -> many warehouses
    private List<Warehouse> managedWarehouses;

	
}
