
package com.biznify.inbound.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class ProductBatchRequestDTO {
    private String skuCode;
    private LocalDateTime manufacturingDate;
    private LocalDateTime expiryDate;
    private String storageCondition;
    private double quantity;
    private Long productId;
    private Long partnerId;
    private Long warehouseId;
    private Long deliveryPartnerId;
    private Long inboundShipmentId;
}
