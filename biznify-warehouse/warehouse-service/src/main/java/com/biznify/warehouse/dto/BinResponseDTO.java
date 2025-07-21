package com.biznify.warehouse.dto;

import com.biznify.warehouse.enums.BinSize;
import com.biznify.warehouse.enums.BinStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinResponseDTO {

	private Long binId;
    private String binCode;
    private Long rackId;

    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;

    private Double volumeCapacity;
    private Double usedVolume;
    private Double availableVolume;

    private Double currentUnitQuantity;
    private Double maxUnitCapacity;

    private BinSize binSize;
    private BinStatus status;
    private String warehouseCode;
    
	private boolean occupied;
	
    private Long productId;
	private ProductDTO product;

	

}
