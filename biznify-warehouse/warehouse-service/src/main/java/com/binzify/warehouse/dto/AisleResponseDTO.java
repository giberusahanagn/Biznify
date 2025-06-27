package com.binzify.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AisleResponseDTO {

	private Long aisleId;
	private String aisleCode;
	private String description;
	private Long warehouseId;

}
