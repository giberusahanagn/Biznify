package com.biznify.warehouse.repository;

import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.enums.BinStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinRepository extends JpaRepository<Bin, Long> {

    // Find bins by status (e.g., EMPTY, FULL)
    List<Bin> findByStatus(BinStatus status);

    // Find all bins in a specific warehouse
    List<Bin> findByRack_Aisle_Warehouse_WarehouseCode(String warehouseCode);

    // Custom JPQL to get all bins with any available space
    @Query("SELECT b FROM Bin b WHERE (b.volumeCapacity - COALESCE(b.usedVolume, 0)) > 0")
    List<Bin> findBinsWithAvailableSpace();

    // Custom JPQL to get bins in a warehouse with available volume
    @Query("SELECT b FROM Bin b WHERE b.rack.aisle.warehouse.warehouseCode = :warehouseCode AND (b.volumeCapacity - COALESCE(b.usedVolume, 0)) > :minVolume")
    List<Bin> findBinsWithAvailableVolume(@Param("warehouseCode") String warehouseCode, @Param("minVolume") double minVolume);

    // ✅ FIXED: Custom JPQL to get bins in a warehouse by warehouseCode and bin status
    @Query("SELECT b FROM Bin b WHERE b.rack.aisle.warehouse.warehouseCode = :warehouseCode AND b.status IN ('EMPTY', 'PARTIALLY_FULL')")
    List<Bin> findBinsWithAvailableSpaceInWarehouse(@Param("warehouseCode") String warehouseCode);
}
