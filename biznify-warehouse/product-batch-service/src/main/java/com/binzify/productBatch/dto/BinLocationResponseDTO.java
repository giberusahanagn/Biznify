package com.binzify.productBatch.dto;

import lombok.Data;

@Data
public class BinLocationResponseDTO {
	private Long binId;
	private String binCode;
	private String aisleCode;
	private String rackCode;
	private Long warehouseId;
	private Double lengthCm;
	private Double widthCm;
	private Double heightCm;
	private Double unitsAllocated;
}