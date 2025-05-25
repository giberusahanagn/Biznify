package com.biznify.warehouse.dto;

import lombok.Data;

@Data
public class BinResponseDTO {
    private String binCode;
    private Double availableSpace;
    private String rackCode;
    private String aisleCode;
    private String warehouseCode;
}
