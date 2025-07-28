package com.biznify.inbound.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inbound_shipments")
@Data
public class InboundShipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "partner_id", nullable = false)
    private Long partnerId;

    @Column(name = "shipment_date", nullable = false)
    private LocalDateTime shipmentDate;


    @Column(name = "invoice_number")
    private String invoiceNumber;



    // Getters and setters
}
