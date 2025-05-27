package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.biznify.warehouse.common.Auditable;
import com.biznify.warehouse.enums.BinStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
public class Bin extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String binCode;

    @Enumerated(EnumType.STRING)
    private BinStatus status; // EMPTY, PARTIALLY_FILLED, FULL

    private Double maxUnitCapacity=0.0;       // in item units
    private Double currentUnitQuantity=0.0;

    private Double volumeCapacity=0.0;        // in cubic meters
    private Double usedVolume = 0.0;
    private Double availableVolume = 0.0;

    private boolean occupied;

    @ManyToOne
    @JoinColumn(name = "rack_id")
    @JsonBackReference("rack-bin")
    private Rack rack;

    public Double getAvailableVolume() {
        return volumeCapacity != null ? volumeCapacity - usedVolume : 0.0;
    }
    
    private Double freeSpace;
}
