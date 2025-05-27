package com.biznify.warehouse.service;

import com.biznify.warehouse.dto.ProductBatchDTO;
import com.biznify.warehouse.entity.InboundShipment;
import com.biznify.warehouse.entity.Partner;
import com.biznify.warehouse.entity.Product;

import java.util.List;

public interface ProductBatchService {
    ProductBatchDTO saveProductBatch(ProductBatchDTO dto);
    List<ProductBatchDTO> getAllProductBatches();
    ProductBatchDTO getProductBatchById(Long id);
    void deleteProductBatch(Long id);

}
