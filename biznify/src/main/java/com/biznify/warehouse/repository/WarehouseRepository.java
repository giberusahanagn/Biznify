package com.biznify.warehouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biznify.warehouse.entity.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
	@EntityGraph(attributePaths = { "aisles", "aisles.racks", "aisles.racks.bins" })
	Optional<Warehouse> findByWarehouseCode(String warehouseCode);
}