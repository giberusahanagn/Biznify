package com.binzify.productBatch.service;

import java.util.List;

import com.binzify.productBatch.dto.ProductBatchRequestDTO;
import com.binzify.productBatch.dto.ProductBatchResponseDTO;
import com.binzify.productBatch.entity.ProductBatch;

public interface ProductBatchService {
	  ProductBatchResponseDTO createBatch(ProductBatchRequestDTO dto);
	    List<ProductBatchResponseDTO> getAll();
	    ProductBatchResponseDTO getById(Long id);
	    ProductBatchResponseDTO updateStatus(Long id, String status);
		ProductBatchResponseDTO storeProductBatch(ProductBatchRequestDTO dto);
		ProductBatch updateBatchQuantity(Long batchId, Double quantity);
		List<ProductBatch> getBatchesByWarehouse(Long warehouseId);
}
