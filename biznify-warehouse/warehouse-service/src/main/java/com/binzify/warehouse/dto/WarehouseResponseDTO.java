package com.binzify.warehouse.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WarehouseResponseDTO {

    private Long id;
    private String name;
    private String warehouseCode;
    private String description;

    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private Double geoLatitude;
    private Double geoLongitude;

    private Double totalCapacity;
    private Double availableCapacity;

    private boolean supportsColdStorage;
    private boolean supportsHazardousMaterials;
    private boolean active;

    private Long managerId;

    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and setters
}
