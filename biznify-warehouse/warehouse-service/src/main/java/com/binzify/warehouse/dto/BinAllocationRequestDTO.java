package com.binzify.warehouse.dto;

import lombok.Data;

@Data
public class BinAllocationRequestDTO {
    private Long warehouseId;
    private Double unitsToStore;
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;
	public String skuCode;
	private Long productId;
	
}