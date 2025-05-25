package com.biznify.warehouse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.biznify.warehouse.dto.CurrentInventoryDTO;
import com.biznify.warehouse.entity.InventoryUpdate;

@Service
public interface InventoryUpdateService {
    List<InventoryUpdate> getInventoryUpdatesByProductId(Long productId);

	InventoryUpdate saveInventoryUpdate(InventoryUpdate inventoryUpdate);
    List<CurrentInventoryDTO> getCurrentInventory();

}
