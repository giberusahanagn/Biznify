package com.biznify.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import com.biznify.warehouse.common.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
@Data
@Entity
public class InventoryUpdate extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String operationType; // "IN", "OUT"
    private Integer quantity;
    private LocalDateTime timestamp;
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    @JsonBackReference("warehouse-inventory")
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "rack_id")
    @JsonBackReference("rack-inventory")
    private Rack rack;

    @ManyToOne
    @JoinColumn(name = "bin_id")
    @JsonBackReference("bin-inventory")
    private Bin bin;
    
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

}
