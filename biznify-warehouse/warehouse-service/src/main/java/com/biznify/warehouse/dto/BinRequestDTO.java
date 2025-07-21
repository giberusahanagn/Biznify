package com.biznify.warehouse.dto;

import com.biznify.warehouse.enums.BinSize;
import com.biznify.warehouse.enums.BinStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinRequestDTO {

	private Long binId;
	
	@NotBlank(message = "Bin code is mandatory")
	private String binCode;

	@NotNull(message = "Rack ID is mandatory")
	private Long rackId;

	private Double lengthCm;
	private Double widthCm;
	private Double heightCm;

	private Double currentUnitQuantity;

	private Double volumeCapacity;
	private Double maxUnitCapacity;
	private Double availableVolume;
	private BinSize binSize;
	private BinStatus status;
	private Double capacity;
	private boolean occupied;
	private Double usedVolume;
	
    private Long productId;
	private ProductDTO product;


}
