package com.biznify.inbound.dto;

import com.biznify.inbound.enums.ProductBatchStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductBatchResponseDTO {

    private Long id; // optional, useful for tracking
    private String batchCode;
    private String invoiceNumber;
    private String skuCode;
    private Double quantity;

    private Long warehouseId;
    private Long partnerId;
    private Long productId;
    private Long deliveryPartnerId;
    private Long inboundShipmentId;

    private String binCode;
    private Long binId;

    private ProductBatchStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
