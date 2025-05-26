package com.biznify.warehouse.dto;

import lombok.Data;

@Data
public class AllocationRequestDTO {
	  private String warehouseCode;
	    private int totalUnits;
	    private Long productId;
}
