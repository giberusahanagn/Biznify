package com.biznify.warehouse.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WarehouseDTO {
    private Long id;
    private String code;
    private String name;
    private String location;
    private String region;
    private String type;                  // Fulfillment, Distribution, etc.
    private String status;                // Active, Maintenance, etc.
    private Double totalCapacity;         // in cubic meters
    private Double availableSpace;        // in cubic meters
    private Boolean temperatureControlled;

    private Integer numberOfRacks;
    private Integer binsPerRack;

    private LocalDate establishedDate;
    private LocalDate lastInspectionDate;

    private String managerName;
    private String contactEmail;
    private String contactPhone;
    
    private List<AisleDTO> aisles;  // Add aisles here

}
