package com.binzify.warehouse.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.binzify.warehouse.dto.WarehouseRequestDTO;
import com.binzify.warehouse.dto.WarehouseResponseDTO;
import com.binzify.warehouse.service.WarehouseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WarehouseResponseDTO> createWarehouse(@Valid @RequestBody WarehouseRequestDTO dto) {
        WarehouseResponseDTO created = warehouseService.createWarehouse(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponseDTO> updateWarehouse(@PathVariable Long id,
                                                                @Valid @RequestBody WarehouseRequestDTO dto) {
        WarehouseResponseDTO updated = warehouseService.updateWarehouse(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponseDTO> getWarehouse(@PathVariable Long id) {
        WarehouseResponseDTO warehouse = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(warehouse);
    }

//    @GetMapping
//    public ResponseEntity<Page<WarehouseResponseDTO>> getWarehouses(Pageable pageable) {
//        Page<WarehouseResponseDTO> page = warehouseService.getAllWarehouses(pageable);
//        return ResponseEntity.ok(page);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}
