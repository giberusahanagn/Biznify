package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.biznify.warehouse.common.Auditable;

@Data
@Entity
public class OutboundBatch extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne
    private ProductBatch productBatch;

    @ManyToOne
    private OutboundShipment outboundShipment;
}
