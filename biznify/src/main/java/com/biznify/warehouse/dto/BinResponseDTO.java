package com.biznify.warehouse.dto;

import lombok.Data;

@Data
public class BinResponseDTO {
    private String binCode;
    private String rackCode;
    private String aisleCode;
    private String warehouseCode;
    private Double availableSpace;  // in m³
    private Integer allocatedUnits; // how many product units can fit here
    // getters & setters
}
