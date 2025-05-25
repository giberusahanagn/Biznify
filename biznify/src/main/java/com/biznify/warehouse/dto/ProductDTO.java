package com.biznify.warehouse.dto;

import lombok.Data;

import java.time.LocalDate;

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
    private LocalDate expiryDate;

    private Long partnerId;
    private String partnerName; // optional: if you want to expose partner info
    public Double getVolumeCm() {
        if (lengthCm != null && widthCm != null && heightCm != null) {
            return lengthCm * widthCm * heightCm;
        }
        return null;
    }
}
