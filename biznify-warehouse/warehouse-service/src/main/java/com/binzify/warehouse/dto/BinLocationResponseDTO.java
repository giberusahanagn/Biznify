package com.binzify.warehouse.dto;

import com.binzify.warehouse.enums.BinStatus;

import lombok.Data;

@Data
public class BinLocationResponseDTO {

	private Long binId;
	private Long warehouseId;
	private Double unitsToStore;
	private Double lengthCm;
	private Double widthCm;
	private Double heightCm;
	private String binCode;
	private BinStatus binStatus;
	private String warehouseCode;
	private String rackCode;
	private String aisleCode;
	private Double unitsAllocated;
	private Double availableVolume;
}