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

import com.binzify.warehouse.dto.BinAllocationRequestDTO;
import com.binzify.warehouse.dto.BinLocationResponseDTO;
import com.binzify.warehouse.dto.BinRequestDTO;
import com.binzify.warehouse.dto.BinResponseDTO;
import com.binzify.warehouse.service.BinService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bins")
public class BinController {

    private final BinService binService;

    public BinController(BinService binService) {
        this.binService = binService;
    }

    @PostMapping
    public ResponseEntity<BinResponseDTO> createBin(@Valid @RequestBody BinRequestDTO dto) {
        BinResponseDTO createdBin = binService.createBin(dto);
        return new ResponseEntity<>(createdBin, HttpStatus.CREATED);
    }

    @PutMapping("/{binId}")
    public ResponseEntity<BinResponseDTO> updateBin(@PathVariable Long binId, @Valid @RequestBody BinRequestDTO dto) {
        BinResponseDTO updatedBin = binService.updateBin(binId, dto);
        return ResponseEntity.ok(updatedBin);
    }

    @GetMapping("/{binId}")
    public ResponseEntity<BinResponseDTO> getBinById(@PathVariable Long binId) {
        return ResponseEntity.ok(binService.getBinById(binId));
    }

    @GetMapping("/rack/{rackId}")
    public ResponseEntity<List<BinResponseDTO>> getBinsByRack(@PathVariable Long rackId) {
        return ResponseEntity.ok(binService.getBinsByRackId(rackId));
    }

    @DeleteMapping("/{binId}")
    public ResponseEntity<Void> deleteBin(@PathVariable Long binId) {
        binService.deleteBin(binId);
        return ResponseEntity.noContent().build();
    }
    
    
    @PostMapping("/allocate")
    public ResponseEntity<List<BinLocationResponseDTO>> allocate(@RequestBody BinAllocationRequestDTO dto) {
        return ResponseEntity.ok(binService.allocateBins(dto));
    }
}
