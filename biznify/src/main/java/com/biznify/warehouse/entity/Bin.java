package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.biznify.warehouse.common.Auditable;
import com.biznify.warehouse.enums.BinStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@RequiredArgsConstructor
public class Bin extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String binCode;

    @Enumerated(EnumType.STRING)
    private BinStatus status; // EMPTY, PARTIALLY_FILLED, FULL

    private Double maxUnitCapacity = 0.0;
    private Double currentUnitQuantity = 0.0;

    private Double volumeCapacity = 0.0; // in cubic meters
    private Double usedVolume = 0.0;     // in cubic meters

    private boolean occupied;

    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;

    @ManyToOne
    @JoinColumn(name = "rack_id")
    @JsonBackReference("rack-bin")
    private Rack rack;

    private Double freeSpace;

     
    @Column(name = "warehouse_code")
    private String warehouseCode;
    @Transient
    public Double getAvailableVolume() {
        double capacity = volumeCapacity != null ? volumeCapacity : 0.0;
        double used = usedVolume != null ? usedVolume : 0.0;
        return capacity - used;
    }
    
    

    @PrePersist
    @PreUpdate
    public void prePersistAndUpdate() {
        // Calculate volume capacity
        // ✅ Automatically calculate volumeCapacity before persist/update

        if (lengthCm != null && widthCm != null && heightCm != null) {
            this.volumeCapacity = (lengthCm * widthCm * heightCm) / 1_000_000.0; // cm³ → m³
        }
        if (usedVolume == null) {
            usedVolume = 0.0;
        }

        // Update warehouse code from rack -> aisle -> warehouse
        if (rack != null && rack.getAisle() != null && rack.getAisle().getWarehouse() != null) {
            this.warehouseCode = rack.getAisle().getWarehouse().getWarehouseCode();
        }
    }



}
