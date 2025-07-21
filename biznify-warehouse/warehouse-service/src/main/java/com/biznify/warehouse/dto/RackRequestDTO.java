package com.biznify.warehouse.dto;


import java.util.List;

import com.biznify.warehouse.enums.RackType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RackRequestDTO {

    @NotBlank(message = "Rack code is mandatory")
    private String rackCode;

    private String description;

    @NotNull(message = "Aisle ID is mandatory")
    private Long aisleId;

    private List<BinRequestDTO> bins;

    private RackType rackType;
    
    // Getters and setters
}

