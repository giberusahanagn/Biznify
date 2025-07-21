package com.biznify.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biznify.warehouse.entity.Aisle;

@Repository
public interface AisleRepository extends JpaRepository<Aisle, Long> {
	  List<Aisle> findByWarehouse_WarehouseId(Long warehouseId);

	  boolean existsByAisleCodeAndWarehouse_WarehouseId(String aisleCode, Long warehouseId);

}
