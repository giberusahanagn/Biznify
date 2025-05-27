package com.biznify.warehouse.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biznify.warehouse.common.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@RequiredArgsConstructor
public class Aisle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String aisleCode;   // Unique system code, e.g., "A-001"

    private String name;        // Display name, e.g., "Frozen Section Aisle"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    @JsonBackReference("warehouse-aisle")
    private Warehouse warehouse;

    @OneToMany(mappedBy = "aisle", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Rack> racks = new HashSet<>();
}
