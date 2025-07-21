package com.biznify.productbatch.service;

import java.util.List;

import com.biznify.productbatch.dto.ProductBatchRequestDTO;
import com.biznify.productbatch.dto.ProductBatchResponseDTO;
import com.biznify.productbatch.entity.ProductBatch;

public interface ProductBatchService {
	  ProductBatchResponseDTO createBatch(ProductBatchRequestDTO dto);
	    List<ProductBatchResponseDTO> getAll();
	    ProductBatchResponseDTO getById(Long id);
	    ProductBatchResponseDTO updateStatus(Long id, String status);
		ProductBatchResponseDTO storeProductBatch(ProductBatchRequestDTO dto);
		ProductBatch updateBatchQuantity(Long batchId, Double quantity);
		List<ProductBatch> getBatchesByWarehouse(Long warehouseId);
}
