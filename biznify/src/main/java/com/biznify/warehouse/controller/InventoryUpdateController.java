package com.biznify.warehouse.controller;

import com.biznify.warehouse.dto.CurrentInventoryDTO;
import com.biznify.warehouse.dto.ProductLocationDTO;
import com.biznify.warehouse.entity.InventoryUpdate;
import com.biznify.warehouse.repository.InventoryUpdateRepository;
import com.biznify.warehouse.service.InventoryUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryUpdateController {

    @Autowired
    private InventoryUpdateService inventoryUpdateService;

    @Autowired
    private InventoryUpdateRepository inventoryUpdateRepository;

    // Create a new inventory update
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InventoryUpdate> createInventoryUpdate(@RequestBody InventoryUpdate inventoryUpdate) {
        InventoryUpdate savedUpdate = inventoryUpdateService.saveInventoryUpdate(inventoryUpdate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUpdate);
    }

    // Get all inventory updates for a product by product ID
    @GetMapping(value = "/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<InventoryUpdate>> getInventoryUpdatesByProductId(@PathVariable Long productId) {
        List<InventoryUpdate> updates = inventoryUpdateService.getInventoryUpdatesByProductId(productId);
        if (updates.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(updates);
    }

    // Get product location with net quantity for a product by product ID
    @GetMapping(value = "/products/{productId}/location", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductLocationDTO> getProductLocation(@PathVariable Long productId) {
        List<ProductLocationDTO> result = inventoryUpdateRepository.findProductLocationWithNetQuantity(productId);
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result.get(0)); // most recent bin with net quantity
    }
    @GetMapping("/current")
    public ResponseEntity<List<CurrentInventoryDTO>> getCurrentInventory() {
        List<CurrentInventoryDTO> currentInventory = inventoryUpdateService.getCurrentInventory();
        if (currentInventory.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(currentInventory);
    }
}
