package com.biznify.warehouse.dto;

import com.biznify.warehouse.enums.ProductSize;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private ProductSize productSize;
    private double unitVolume;
}
