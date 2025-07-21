package com.biznify.warehouse.dto;

import com.biznify.warehouse.enums.RackType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RackResponseDTO {

    private Long id;
    private String rackCode;
    private String description;

    private Long aisleId;
    private RackType rackType;

    private List<BinResponseDTO> bins;

    // Getters and setters
}
