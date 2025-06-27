package com.binzify.warehouse.dto;


import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class WarehouseRequestDTO {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Warehouse code is mandatory")
    private String warehouseCode;

    private String description;
                                  
    private String addressLine;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private Double geoLatitude;
    private Double geoLongitude;

    @NotNull(message = "Total capacity is mandatory")
    @PositiveOrZero(message = "Total capacity cannot be negative")
    private Double totalCapacity;

    @NotNull(message = "Available capacity is mandatory")
    @PositiveOrZero(message = "Available capacity cannot be negative")
    private Double availableCapacity;

    private boolean supportsColdStorage;
    private boolean supportsHazardousMaterials;
    private boolean active;
    private List<AisleRequestDTO> aisles;
    private Long managerId;

	

}
