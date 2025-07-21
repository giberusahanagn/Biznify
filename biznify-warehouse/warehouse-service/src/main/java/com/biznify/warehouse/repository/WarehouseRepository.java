package com.biznify.warehouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biznify.warehouse.entity.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
	Optional<Warehouse> findByWarehouseCode(String warehouseCode);
    boolean existsByWarehouseCode(String warehouseCode);

}