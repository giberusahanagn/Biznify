package com.biznify.warehouse.dto;

import java.util.List;

import com.biznify.warehouse.enums.AisleSize;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class AisleDTO {
    private String aisleCode;
    private AisleSize aisleSize; // Should match your enum: "SMALL", "MEDIUM", "LARGE"
    private String description;

    private List<RackDTO> racks;
}