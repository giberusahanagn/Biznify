package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.biznify.warehouse.common.Auditable;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
public class Warehouse extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "warehouse_code")
    private String warehouseCode;                    // Unique warehouse code
    private String name;
    private String location;
    private String region;

    private String type;                    // e.g. Fulfillment, Storage, Distribution
    private String status;                  // e.g. Active, Under Maintenance, Closed

    private Double totalCapacity;           // in cubic meters
    private Double availableSpace;          // updated based on inventory

    private Boolean temperatureControlled;

    private Integer numberOfRacks;
    private Integer binsPerRack;            // Optional: used to auto-generate bins

    private LocalDate establishedDate;
    private LocalDate lastInspectionDate;

    private String managerName;
    private String contactEmail;
    private String contactPhone;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aisle> aisles = new ArrayList<>();

   
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;               // Optional: actual manager user
}
