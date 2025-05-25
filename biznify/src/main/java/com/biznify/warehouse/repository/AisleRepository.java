package com.biznify.warehouse.repository;

import com.biznify.warehouse.entity.Aisle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AisleRepository extends JpaRepository<Aisle, Long> {
    List<Aisle> findByWarehouse_WarehouseCode(String warehouseCode);
}
