package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import com.biznify.warehouse.common.Auditable;

@Data
@Entity
public class PartnerWarehouseAgreement extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The partner who owns the products
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    // The warehouse where products are stored
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    // Agreement start and end date
    private LocalDate startDate;
    private LocalDate endDate;

    // Maximum storage volume allowed (e.g., cubic meters)
    private Double maxStorageVolume;

    // Maximum number of SKUs allowed in warehouse for this partner
    private Integer maxSKUCount;

    // Storage fee per unit volume (e.g., per cubic meter)
    private Double storageFeePerUnitVolume;

    // Handling fee per inbound or outbound shipment
    private Double handlingFee;

    // Payment terms description or code (e.g., net 30)
    private String paymentTerms;

    // Special conditions or notes
    @Column(length = 1000)
    private String specialConditions;

    // List of allowed delivery partners (optional)
    @ManyToMany
    @JoinTable(
        name = "agreement_delivery_partner",
        joinColumns = @JoinColumn(name = "agreement_id"),
        inverseJoinColumns = @JoinColumn(name = "delivery_partner_id")
    )
    private List<DeliveryPartner> allowedDeliveryPartners;
}
