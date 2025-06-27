package com.binzify.productBatch.dto;

import lombok.Data;

@Data
public class BinAllocationRequestDTO {
	private Double lengthCm;
	private Double widthCm;
	private Double heightCm;
	private Double unitsToStore;
	private String skuCode;
	private Long warehouseId;
	
}
