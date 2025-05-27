package com.biznify.warehouse.dto;

import lombok.Data;

@Data
public class BinAllocationResponseDTO {

	private String binCode;
	private String rackCode;
	private String aisleCode;
	private String warehouseCode;
	private double availableSpace;
	private double allocatedUnits;
	// getters and setters

	public BinAllocationResponseDTO() {
		// TODO Auto-generated constructor stub
	}
	public BinAllocationResponseDTO(String binCode2, double allocated, String warehouseCode2, String rackCode2,
			String aisleCode2) {
		// TODO Auto-generated constructor stub
	}

	
}
