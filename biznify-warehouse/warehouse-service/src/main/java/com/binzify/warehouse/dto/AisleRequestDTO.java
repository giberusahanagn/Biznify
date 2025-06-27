package com.binzify.warehouse.dto;

import java.util.List;

import com.binzify.warehouse.enums.AisleSize;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AisleRequestDTO {

    @NotBlank(message = "Aisle code is mandatory")
    private String aisleCode;

    private String description;

    @NotNull(message = "Warehouse ID is mandatory")
    private Long warehouseId;

    private List<RackRequestDTO> racks;
    
    private AisleSize aisleSize;

    
}
