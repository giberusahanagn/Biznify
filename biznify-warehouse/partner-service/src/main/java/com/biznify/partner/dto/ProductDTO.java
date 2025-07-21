package com.biznify.partner.dto;

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
    private String expiryDate; // Use String for dates for serialization safety
    private Long partnerId;
    private Double volumeInCm;
    private String createdAt; // Use String for dates for serialization safety
    private Double weightKg;
    private String updatedAt; // Use String for dates for serialization safety

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getLengthCm() {
        return lengthCm;
    }

    public void setLengthCm(Double lengthCm) {
        this.lengthCm = lengthCm;
    }

    public Double getWidthCm() {
        return widthCm;
    }

    public void setWidthCm(Double widthCm) {
        this.widthCm = widthCm;
    }

    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public Boolean getTemperatureSensitive() {
        return temperatureSensitive;
    }

    public void setTemperatureSensitive(Boolean temperatureSensitive) {
        this.temperatureSensitive = temperatureSensitive;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public Double getVolumeInCm() {
        return volumeInCm;
    }

    public void setVolumeInCm(Double volumeInCm) {
        this.volumeInCm = volumeInCm;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}