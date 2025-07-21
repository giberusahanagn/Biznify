package com.biznify.warehouse.service;


import com.biznify.warehouse.dto.WarehouseRequestDTO;
import com.biznify.warehouse.dto.WarehouseResponseDTO;

public interface WarehouseService {

    WarehouseResponseDTO createWarehouse(WarehouseRequestDTO warehouseRequest);

    WarehouseResponseDTO updateWarehouse(Long warehouseId, WarehouseRequestDTO warehouseRequest);

    WarehouseResponseDTO getWarehouseById(Long warehouseId);


    void deleteWarehouse(Long id);

    // Custom business logic methods can be added here, e.g., updateCapacity
}
