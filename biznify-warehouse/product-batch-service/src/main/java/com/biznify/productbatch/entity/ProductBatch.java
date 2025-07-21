package com.biznify.productbatch.entity;

import java.time.LocalDateTime;

import com.biznify.productbatch.enums.ProductBatchStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBatch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long partnerId; // ID of the product owner (partner)
	private Long productId; // ID of the product
	private String batchCode;
	private String skuCode;

	private LocalDateTime manufacturingDate;
	private LocalDateTime expiryDate;

	private Double lengthCm;
	private Double widthCm;
	private Double heightCm;
	private Double weightKg;

	private String storageCondition; // e.g., NORMAL, COOLING

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Enumerated(EnumType.STRING)
	private ProductBatchStatus status;

	private Long deliveryPartnerId;
	private Long inboundShipmentId;
	private Long outboundShipmentId; // optional
	private String invoiceNumber;

	private String binCode;
	private Long binId;
	private Long warehouseId;

	private Double quantity;

}
