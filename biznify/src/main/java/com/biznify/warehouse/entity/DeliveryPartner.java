package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.biznify.warehouse.common.Auditable;

@Data
@Entity
public class DeliveryPartner extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Company name (e.g., BlueDart, Delhivery, FedEx)
    private String name;

    // Official contact email
    private String email;

    // Customer service or operations contact
    private String contactNumber;

    // Optional: Transport license or registration details
    private String registrationNumber;

    // Address or HQ
    private String address;

    // Inbound shipments they handled
    @OneToMany(mappedBy = "deliveryPartner")
    private List<InboundShipment> inboundShipments;

//    // Future use: outbound shipments
//    @OneToMany(mappedBy = "deliveryPartner")
//    private List<OutboundShipment> outboundShipments;
}
