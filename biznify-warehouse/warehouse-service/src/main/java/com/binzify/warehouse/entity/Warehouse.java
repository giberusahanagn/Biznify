package com.binzify.warehouse.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.binzify.warehouse.enums.AisleSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "warehouses")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long warehouseId;

    // Core Identifiers
    @Column(nullable = false, unique = true)
    private String warehouseCode;             // e.g., "WH-001"

    @Column(nullable = false)
    private String name;             // "Central Warehouse"

    // Location & Contact
    @Column(nullable = false)
    private String addressLine;      // Street address

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String country;

    private String contactNumber;
    private String email;

    // Physical Characteristics
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;

    private Double totalCapacityVolume;    // in cm³ (cubic centimeters)
    private Double availableCapacityVolume;
    private Double maxWeightCapacity;      // in kilograms

    // Operational
    private String status;           // "ACTIVE", "INACTIVE", "MAINTENANCE", etc.
    private String managerName;      // Person responsible

    private String description;      // Purpose, notes, etc.
    
    
    // Relationships (optional, but common)
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aisle> aisles = new ArrayList<>();

    // Audit Fields
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

   
    
    
  
}