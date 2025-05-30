package com.biznify.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.entity.Product;
import com.biznify.warehouse.entity.ProductBatch;

public interface ProductBatchRepository extends JpaRepository<ProductBatch, Long> {

	List<ProductBatch> findByProduct_Id(Long productId);

	@Query("SELECT COALESCE(SUM(pb.quantity), 0) FROM ProductBatch pb WHERE pb.bin = :bin")
	double getTotalUnitsInBin(Bin bin);

	
}
