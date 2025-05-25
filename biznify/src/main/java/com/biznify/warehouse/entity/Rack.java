package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import com.biznify.warehouse.common.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
public class Rack extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String rackCode; // Unique code, e.g., "RACK-01"

	private Double height; // meters
	private Double width; // meters
	private Double depth; // meters

	private Double availableCapacity; // dynamically updated

	private Integer numberOfLevels; // number of shelf levels
	private String type; // e.g., "pallet", "cold-storage"

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "aisle_id")
	@JsonBackReference("aisle-rack")
	private Aisle aisle; // NEW: Rack belongs to an aisle

	@OneToMany(mappedBy = "rack", cascade = CascadeType.ALL)
	@JsonManagedReference("rack-bin")
	private List<Bin> bins;

	public Double getTotalCapacity() {
		if (height != null && width != null && depth != null) {
			return height * width * depth;
		}
		return 0.0;
	}
	@ManyToOne
	@JoinColumn(name = "warehouse_id")
	private Warehouse warehouse;
}
