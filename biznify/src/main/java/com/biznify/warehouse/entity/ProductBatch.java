package com.biznify.warehouse.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.biznify.warehouse.common.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
public class ProductBatch extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@OneToMany(mappedBy = "productBatch", cascade = CascadeType.ALL)
	private List<ProductBatchBinMapping> binMappings = new ArrayList<>();

	private LocalDate receivedDate;

	private String batchNumber;

	private LocalDate manufactureDate;

	private LocalDate expirationDate;

	// optional: who handled this batch
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee handledBy;

	@ManyToOne
	@JoinColumn(name = "inbound_shipment_id")
	private InboundShipment inboundShipment;

//	private Partner partner;
	private Double volume_per_unit;

	private Double quantity;
}
