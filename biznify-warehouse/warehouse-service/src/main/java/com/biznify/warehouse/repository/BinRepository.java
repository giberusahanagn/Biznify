package com.biznify.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.biznify.warehouse.entity.Bin;

@Repository

public interface BinRepository extends JpaRepository<Bin, Long> {

    List<Bin> findByRack_RackId(Long rackId);

    List<Bin> findByRack_Aisle_Warehouse_WarehouseId(Long warehouseId);

    boolean existsByBinCodeAndRack_RackId(String binCode, Long rackId);

    @Query("SELECT b FROM Bin b " +
    	       "JOIN b.rack r " +
    	       "JOIN r.aisle a " +
    	       "JOIN a.warehouse w " +
    	       "WHERE w.warehouseId = :warehouseId AND b.availableVolume > 0")
    	List<Bin> findAvailableBins(@Param("warehouseId") Long warehouseId);


    @Query("SELECT b FROM Bin b WHERE b.rack.aisle.warehouse.warehouseId = :warehouseId")
    List<Bin> findAllByWarehouseId(@Param("warehouseId") Long warehouseId);
}
