package com.biznify.product.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import org.springframework.data.annotation.CreatedBy;

@Data
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

}
