package com.biznify.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AisleResponseDTO {

	private Long aisleId;
	private String aisleCode;
	private String description;
	private Long warehouseId;
private List<RackResponseDTO> racks;
}
