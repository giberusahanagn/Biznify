package com.biznify.warehouse.entity;

import java.time.LocalDate;

import com.biznify.warehouse.common.Auditable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class ProductBatch extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "bin_id")
    private Bin bin;

    private double quantity;

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

}
