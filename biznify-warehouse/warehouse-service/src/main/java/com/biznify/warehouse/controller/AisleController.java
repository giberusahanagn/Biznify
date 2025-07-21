package com.biznify.warehouse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biznify.warehouse.dto.AisleRequestDTO;
import com.biznify.warehouse.dto.AisleResponseDTO;
import com.biznify.warehouse.service.AisleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/aisles")
public class AisleController {

    private final AisleService aisleService;

    public AisleController(AisleService aisleService) {
        this.aisleService = aisleService;
    }

    @PostMapping
    public ResponseEntity<AisleResponseDTO> createAisle(@Valid @RequestBody AisleRequestDTO dto) {
        return new ResponseEntity<>(aisleService.createAisle(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AisleResponseDTO> updateAisle(@PathVariable Long id, @Valid @RequestBody AisleRequestDTO dto) {
        return ResponseEntity.ok(aisleService.updateAisle(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AisleResponseDTO> getAisleById(@PathVariable Long id) {
        return ResponseEntity.ok(aisleService.getAisleById(id));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<AisleResponseDTO>> getAislesByWarehouse(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(aisleService.getAislesByWarehouseId(warehouseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAisle(@PathVariable Long id) {
        aisleService.deleteAisle(id);
        return ResponseEntity.noContent().build();
    }
}
