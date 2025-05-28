package com.biznify.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.entity.Product;
import com.biznify.warehouse.entity.ProductBatchBinMapping;

@Repository
public interface ProductBatchBinMappingRepository extends JpaRepository<ProductBatchBinMapping, Long> {
	List<ProductBatchBinMapping> findByBinAndProductBatch_Product(Bin bin, Product product);


    @Query("SELECT COALESCE(SUM(pbm.quantity), 0) FROM ProductBatchBinMapping pbm WHERE pbm.bin = :bin")
    Double getTotalUnitsInBin(@Param("bin") Bin bin);
}
