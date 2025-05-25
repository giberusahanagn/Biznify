package com.biznify.warehouse.dto;

import lombok.Data;
import java.util.List;

@Data
public class RackDTO {
	private Long id;
	private String rackCode;
	private Double height;
	private Double width;
	private Double depth;
	private Double totalCapacity; // derived: h*w*d
	private Double availableCapacity;
	private Integer numberOfLevels;
	private String type;
	private List<BinDTO> bins;
}
