package com.binzify.warehouse.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.binzify.warehouse.enums.BinSize;
import com.binzify.warehouse.enums.BinStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bins", uniqueConstraints = @UniqueConstraint(columnNames = { "rack_id", "code" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bin implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long BinId;

	@Column(nullable = false)
	private String binCode; // Unique within a rack (e.g., "BIN-01")

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rack_id", nullable = false)
	private Rack rack;

	// Physical dimensions
	private Double lengthCm;
	private Double widthCm;
	private Double heightCm;

	@Column(nullable = false)
	private Double volumeCapacity; // Total capacity (cm³)

	@Column(nullable = false)
	private Double usedVolume; // Used space (cm³)

	@Column(nullable = false)
	private Double availableVolume; // Remaining space (cm³)

	@Column(nullable = false)
	private Double maxUnitCapacity; // Maximum number of units (e.g., 1000 pieces)

	@Column(nullable = false)
	private Double currentUnitQuantity; // Current number of units stored

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BinStatus status; // Enum: EMPTY, PARTIAL, FULL, BLOCKED

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BinSize binSize; // Enum: SMALL, MEDIUM, LARGE

	@Column(nullable = false)
	private Boolean occupied; // True if at least one unit is stored

	private String storedSkuCode; // SKU code if single SKU per bin

	private String description; // Optional notes or comments

	private Long productId; // Only the ID

	@Column(updatable = false)
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@PrePersist
	public void onCreate() {
	    createdAt = updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	public void onUpdate() {
	    updatedAt = LocalDateTime.now();
	}
}