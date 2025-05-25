package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.biznify.warehouse.common.Auditable;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
public class Partner extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String email;
	private String contactNumber;
	private String status; // e.g., Active, Inactive
	private String websiteUrl;

	@OneToMany(mappedBy = "partner")
	@JsonManagedReference("partner-product")
	private List<Product> products;
}
