package com.biznify.warehouse.repository;

import com.biznify.warehouse.dto.BinDTO;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.entity.Rack;
import com.biznify.warehouse.enums.BinStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BinRepository extends JpaRepository<Bin, Long> {
	  List<Bin> findByStatus(BinStatus status);

	  List<Bin> findByRack_Warehouse_WarehouseCodeAndAvailableVolumeGreaterThan(String warehouseCode, double volume);
	
	    List<Bin> findByAvailableSpaceGreaterThan(Double minVolume);

}
