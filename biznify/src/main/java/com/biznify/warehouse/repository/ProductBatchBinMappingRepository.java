package com.biznify.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biznify.warehouse.entity.ProductBatchBinMapping;

public interface ProductBatchBinMappingRepository extends JpaRepository<ProductBatchBinMapping, Long> {
}
