package com.biznify.productbatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biznify.productbatch.entity.ProductBatch;

public interface ProductBatchRepository extends JpaRepository<ProductBatch, Long> {
    boolean existsByBatchCode(String batchCode);

    boolean existsBySkuCode(String skuCode);
    boolean existsById(Long id);  
    
    List<ProductBatch> getByWarehouseId(Long warehouseId);
 
}