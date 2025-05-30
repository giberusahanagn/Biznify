package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.biznify.warehouse.common.Auditable;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@RequiredArgsConstructor
public class Warehouse extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Aisle> aisles;

   
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;               // Optional: actual manager user
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

}
