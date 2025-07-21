package com.biznify.warehouse.dto;

import com.biznify.warehouse.enums.BinSize;
import com.biznify.warehouse.enums.BinStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BinDTO {
	
	private Long binId;
    private String binCode;

    private BinStatus status;
    private BinSize binSize;

    private Double maxUnitCapacity;
    private Double currentUnitQuantity;

    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;
    private Double availableVolume;

    private boolean occupied;
	private Double usedVolume;
	
    private Long productId;

	private ProductDTO product;


}
