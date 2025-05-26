package com.biznify.warehouse.dto;


import lombok.Data;

@Data
public class StorageRequestDTO {
	 private String warehouseCode;
	    private Long productId;
	    private Integer quantity; // MUST be named exactly "quantity"
}