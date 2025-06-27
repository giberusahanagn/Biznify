package com.binzify.productBatch.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.binzify.productBatch.dto.ProductBatchRequestDTO;
import com.binzify.productBatch.dto.ProductBatchResponseDTO;
import com.binzify.productBatch.entity.ProductBatch;
import com.binzify.productBatch.service.ProductBatchService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/product-batches")
@RequiredArgsConstructor
public class ProductBatchController {

    private final ProductBatchService productBatchService;

    @PostMapping
    public ResponseEntity<ProductBatchResponseDTO> create(@RequestBody ProductBatchRequestDTO dto) {
        return ResponseEntity.ok(productBatchService.createBatch(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductBatchResponseDTO>> getAll() {
        return ResponseEntity.ok(productBatchService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBatchResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productBatchService.getById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ProductBatchResponseDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(productBatchService.updateStatus(id, status));
    }
    
    
    @PostMapping("/store")
    public ResponseEntity<ProductBatchResponseDTO> storeBatch(@RequestBody ProductBatchRequestDTO dto) {
    	System.out.println("inside");
        ProductBatchResponseDTO response = productBatchService.storeProductBatch(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping("/update/{batchId}")
    public ResponseEntity<ProductBatch> updateQuantity(@PathVariable Long batchId, @RequestParam Double quantity) {
        ProductBatch batch = productBatchService.updateBatchQuantity(batchId, quantity);
        return ResponseEntity.ok(batch);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<ProductBatch>> getBatchesByWarehouse(@PathVariable Long warehouseId) {
        List<ProductBatch> batches = productBatchService.getBatchesByWarehouse(warehouseId);
        return ResponseEntity.ok(batches);
    }
   
}
