package com.binzify.warehouse.dto;

import lombok.Data;

@Data
public class ProductBatchRequestDTO {

	
    private Long productId;
    private Long inboundShipmentId;
    private int quantity;
}
