package com.binzify.warehouse.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.binzify.warehouse.enums.RackType;

@Data
@Setter
@Getter
public class RackDTO {
    private String rackCode;
    private Double heightCm;
    private Double widthCm;
    private Double depthCm;
    private Integer numberOfLevels;
    private RackType rackType;

    private List<BinDTO> bins;
}
