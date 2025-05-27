package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.biznify.warehouse.common.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@RequiredArgsConstructor
public class Rack extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String rackCode; // Unique code, e.g., "RACK-01"

	private Double heightCm; // meters
	private Double widthCm; // meters
	private Double depthCm; // meters

	private Double availableCapacity; // dynamically updated

	private Integer numberOfLevels; // number of shelf levels
	private String type; // e.g., "pallet", "cold-storage"

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aisle_id")
	@JsonBackReference("aisle-rack")
	private Aisle aisle; // NEW: Rack belongs to an aisle

	@OneToMany(mappedBy = "rack", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Bin> bins = new HashSet<>();

	public Double getTotalCapacity() {
		if (heightCm != null && widthCm != null && depthCm != null) {
			return heightCm * widthCm * depthCm;
		}
		return 0.0;
	}
	@ManyToOne
	@JoinColumn(name = "warehouse_id")
	private Warehouse warehouse;
	
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (!(o instanceof Rack)) return false;
	        Rack rack = (Rack) o;
	        return Objects.equals(id, rack.id) || Objects.equals(rackCode, rack.rackCode);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(id, rackCode); // No collection-based hash
	    }
	
}
