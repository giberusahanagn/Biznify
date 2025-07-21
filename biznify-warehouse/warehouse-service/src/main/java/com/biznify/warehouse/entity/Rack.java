package com.biznify.warehouse.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.biznify.warehouse.enums.RackType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor

@Entity
@Table(name = "racks")
public class Rack {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rackId;

    @Column(nullable = false)
    private String rackCode;               // "R1", "R2", etc.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aisle_id", nullable = false)
    private Aisle aisle;

    private Double lengthCm;
    private Double widthCm;
    private Double heightCm;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RackType rackType;         // Enum: PALLET, SHELF, MEZZANINE, etc.

    private String description;

    @OneToMany(mappedBy = "rack", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bin> bins = new ArrayList<>();
    
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}