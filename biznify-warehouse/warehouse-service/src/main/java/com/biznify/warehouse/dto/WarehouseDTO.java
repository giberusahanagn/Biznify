package com.biznify.warehouse.dto;

import java.util.List;

import lombok.Data;

@Data
public class WarehouseDTO {
    private Long warehouseId;
    private String name;
    private String warehouseCode;
    private String description;

    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private String geoLatitude;
    private String geoLongitude;

    private double totalCapacity;
    private double availableCapacity;
    private boolean supportsColdStorage;
    private boolean supportsHazardousMaterials;
    private boolean active;

    private Long managerId;

    private List<AisleDTO> aisles;
}
