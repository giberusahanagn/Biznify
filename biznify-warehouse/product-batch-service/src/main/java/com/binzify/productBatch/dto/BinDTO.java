package com.binzify.productBatch.dto;

import lombok.Data;

@Data
public class BinDTO {
	private Long id;
	private String binCode;
	private Double availableVolume;
	private Long warehouseId;

	

}