package com.binzify.warehouse.dto;

import com.binzify.warehouse.enums.RackType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    // Getters and setters
}
