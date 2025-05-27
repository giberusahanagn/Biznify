package com.biznify.warehouse.controller;

import com.biznify.warehouse.dto.ProductBatchAllocationRequest;
import com.biznify.warehouse.dto.ProductBatchDTO;
import com.biznify.warehouse.service.ProductBatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-batches")
public class ProductBatchController {

    @Autowired
    private ProductBatchService productBatchService;

    @PostMapping
    public ResponseEntity<ProductBatchDTO> createProductBatch(@RequestBody ProductBatchDTO dto) {
        ProductBatchDTO savedDTO = productBatchService.saveProductBatch(dto);
        return ResponseEntity.ok(savedDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProductBatchDTO>> getAllProductBatches() {
        return ResponseEntity.ok(productBatchService.getAllProductBatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBatchDTO> getProductBatchById(@PathVariable Long id) {
        return ResponseEntity.ok(productBatchService.getProductBatchById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductBatch(@PathVariable Long id) {
        productBatchService.deleteProductBatch(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/product/{productId}/batches")
    public ResponseEntity<List<ProductBatchDTO>> getBatchesByProduct(@PathVariable Long productId) {
        List<ProductBatchDTO> batches = productBatchService.getBatchesByProductId(productId);
        return ResponseEntity.ok(batches);
    }
   
}
