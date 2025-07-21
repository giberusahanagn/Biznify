package com.biznify.product.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedBy;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String sku;
	private String category;

	private Double volumeInCm;

	private Double lengthCm;
	private Double widthCm;
	private Double heightCm;

	@Column(name = "weight_kg", nullable = false)
	private Double weightKg;
	private Boolean temperatureSensitive;
	private LocalDate expiryDate;

	// Instead of Partner entity, store partnerId only
	@Column(name = "partner_id", nullable = false)
	private Long partnerId;
	private LocalDate createdAt;

	private LocalDate updatedAt;

	@Transient
	public Double getUnitVolumeInCubicMeters() {
		if (lengthCm == null || widthCm == null || heightCm == null) {
			return null;
		}
		
		return (lengthCm * widthCm * heightCm) / 1_000_000.0;
	}
	
	@PrePersist
	public void onCreate() {
	    this.createdAt = LocalDate.now();
	    this.updatedAt = LocalDate.now();
	}

	@PreUpdate
	public void onUpdate() {
	    this.updatedAt = LocalDate.now();
	}

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

	public Double getVolumeInCm() {
		return volumeInCm;
	}

	public void setVolumeInCm(Double volumeInCm) {
		this.volumeInCm = volumeInCm;
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

	public Double getWeightKg() {
		return weightKg;
	}

	public void setWeightKg(Double weightKg) {
		this.weightKg = weightKg;
	}

	public Boolean getTemperatureSensitive() {
		return temperatureSensitive;
	}

	public void setTemperatureSensitive(Boolean temperatureSensitive) {
		this.temperatureSensitive = temperatureSensitive;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}
}
