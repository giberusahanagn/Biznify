package com.biznify.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductLocationDTO {
	private String productName;
	private String warehouseName;
	private String rackCode;
	private String binCode;
    private Double quantity;
  
}
