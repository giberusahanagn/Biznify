package com.biznify.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ProductWithPartnerDTO {
    private Long id;
    private String name;
    private String sku;
    private String category;
    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;
    private Boolean temperatureSensitive;
    private String expiryDate; // Use String for dates
    private Long partnerId;
    private Double volumeInCm;
    private String createdAt; // Use String for dates
    private Double weightKg;
    private String updatedAt; // Use String for dates

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PartnerDTO partner;
}