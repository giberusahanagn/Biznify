package com.binzify.productBatch.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private Long warehouseId;
    private String name;
    private String sku;
    private String category;
    private Double volumeInCm;
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;
    private Boolean temperatureSensitive;
    private LocalDate expiryDate;
    private Long partnerId;

	// storing bin
	private Long binId;
	private Double quantity;
	private LocalDateTime manufactureDate;
}
