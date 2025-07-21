package com.biznify.productbatch.dto;

import java.time.LocalDateTime;

import com.biznify.productbatch.enums.ProductBatchStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBatchResponseDTO {
    private Long id;
    private Long partnerId;
    private Long productId;
    private String batchCode;
    private String skuCode;
    private LocalDateTime manufacturingDate;
    private LocalDateTime expiryDate;
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;
    private Double weightKg;
    private String storageCondition;
    private ProductBatchStatus status;
    private Long deliveryPartnerId;
    private Long inboundShipmentId;
    private Long outboundShipmentId;
    private String invoiceNumber;
    private Long binId;
    private String binCode;
    private Double quantity;
	

    // Optionally add bin details if fetched via WebClient
   // private BinDTO binDetails;
}
