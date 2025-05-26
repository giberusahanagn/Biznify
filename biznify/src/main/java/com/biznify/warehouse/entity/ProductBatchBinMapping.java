package com.biznify.warehouse.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import com.biznify.warehouse.common.Auditable;

@Data
@Entity
public class ProductBatchBinMapping extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProductBatch productBatch;

    @ManyToOne
    private InboundShipment inboundShipment;

    @ManyToOne
    private Aisle aisle;

    @ManyToOne
    private Rack rack;

    @ManyToOne
    private Bin bin;

    private double quantityStored;

    private LocalDateTime storedAt;
}
