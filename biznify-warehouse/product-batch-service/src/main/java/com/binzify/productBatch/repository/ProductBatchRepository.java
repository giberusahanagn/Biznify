package com.binzify.productBatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.binzify.productBatch.entity.ProductBatch;

public interface ProductBatchRepository extends JpaRepository<ProductBatch, Long> {
    boolean existsByBatchCode(String batchCode);

    boolean existsBySkuCode(String skuCode);
    boolean existsById(Long id);  
    
    List<ProductBatch> getByWarehouseId(Long warehouseId);
 
}