package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.biznify.warehouse.common.Auditable;

@Data
@Entity
public class OutboundShipment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dispatchDate;
    private String destination;
    private String status;

    @ManyToOne
    private Warehouse warehouse;

    @ManyToOne
    private DeliveryPartner deliveryPartner;

    @OneToMany(mappedBy = "outboundShipment", cascade = CascadeType.ALL)
    private List<OutboundBatch> outboundBatches;
}
