package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import com.biznify.warehouse.common.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
public class InboundShipment extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // When the shipment physically arrives
    private LocalDateTime arrivalDate;

    // Invoice or delivery note number from supplier
    private String invoiceNumber;

    // Optional reference or PO number
    private String referenceNumber;

    // Status of the shipment: RECEIVED, PENDING, PARTIALLY_RECEIVED
    private String status;

    // Name of driver or delivery contact
    private String deliveryContactName;

    // Vehicle or tracking information
    private String transportDetails;

    // Partner who owns the products
    @ManyToOne
    @JoinColumn(name = "partner_id", nullable = false)
    private Partner partner;

    // Delivery partner (logistics provider)
    @ManyToOne
    @JoinColumn(name = "delivery_partner_id")
    @JsonBackReference
    private DeliveryPartner deliveryPartner;
    // Warehouse where the shipment is received
    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    // Employee who received the shipment
    @ManyToOne
    @JoinColumn(name = "received_by_employee_id")
    private Employee receivedBy;

    // One shipment contains many product batches
    @OneToMany(mappedBy = "inboundShipment", cascade = CascadeType.ALL)
    private List<ProductBatch> productBatches;

    // Any special remarks (e.g., damaged, missing items)
    private String remarks;
}
