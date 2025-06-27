package com.biznify.product.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String sku;
    private String category;
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;
    private Boolean temperatureSensitive;
    private String expiryDate; // Use String for dates so both services can communicate easily
    private Long partnerId;
    private Double volumeInCm;
    private String createdAt; // Use String for dates so both services can communicate easily
    private Double weightKg;
    private String updatedAt; // Use String for dates so both services can communicate easily
}