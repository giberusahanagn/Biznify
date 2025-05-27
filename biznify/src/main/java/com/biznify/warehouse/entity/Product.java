package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import com.biznify.warehouse.common.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.Date;


@Data
@Entity
public class Product extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String sku;
    private String category;

    @NotNull
    @Positive
    private Double volumeInCm;
    @NotNull
    @Positive
    private Double lengthCm; // in centimeters

    @NotNull
    @Positive
    private Double widthCm ;

    @NotNull
    @Positive
    private Double heightCm;
    private Boolean temperatureSensitive;
    private LocalDate expiryDate;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    @JsonBackReference("partner-product")
    private Partner partner;
    
 
    @Transient
    public Double getUnitVolumeInCubicMeters() {
        // Convert cm³ to m³: divide by 1,000,000
        return (lengthCm * widthCm * heightCm) / 1_000_000.0;
    }

}
