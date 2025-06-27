package com.binzify.warehouse.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.binzify.warehouse.enums.AisleSize;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "aisles")
public class Aisle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long aisleId;

	@Column(nullable = false)
	private String aisleCode; // "A1", "B2", etc.

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warehouse_id", nullable = false)
	private Warehouse warehouse;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AisleSize aisleSize; // Enum: SMALL, MEDIUM, LARGE

	private String description;

	@OneToMany(mappedBy = "aisle", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Rack> racks = new ArrayList<>();

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