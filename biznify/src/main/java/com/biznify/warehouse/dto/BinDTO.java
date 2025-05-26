package com.biznify.warehouse.dto;

import com.biznify.warehouse.enums.BinStatus;
import lombok.Data;

@Data
public class BinDTO {
    private Long id;
    private String binCode;
    private BinStatus status;
    private Double maxUnitCapacity;
    private Double currentUnitQuantity;
    private Double volumeCapacity;     // in m³
    private Double usedVolume;
    private Double availableVolume;    // will be set manually in service/mapper
    private boolean occupied;
}
