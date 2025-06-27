package com.binzify.warehouse.controller;

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

import com.binzify.warehouse.dto.RackRequestDTO;
import com.binzify.warehouse.dto.RackResponseDTO;
import com.binzify.warehouse.service.RackService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/racks")
public class RackController {

    private final RackService rackService;

    public RackController(RackService rackService) {
        this.rackService = rackService;
    }

    @PostMapping
    public ResponseEntity<RackResponseDTO> createRack(@Valid @RequestBody RackRequestDTO dto) {
        return new ResponseEntity<>(rackService.createRack(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RackResponseDTO> updateRack(@PathVariable Long id, @Valid @RequestBody RackRequestDTO dto) {
        return ResponseEntity.ok(rackService.updateRack(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RackResponseDTO> getRackById(@PathVariable Long id) {
        return ResponseEntity.ok(rackService.getRackById(id));
    }

    @GetMapping("/aisle/{aisleId}")
    public ResponseEntity<List<RackResponseDTO>> getRacksByAisle(@PathVariable Long aisleId) {
        return ResponseEntity.ok(rackService.getRacksByAisleId(aisleId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRack(@PathVariable Long id) {
        rackService.deleteRack(id);
        return ResponseEntity.noContent().build();
    }
}
