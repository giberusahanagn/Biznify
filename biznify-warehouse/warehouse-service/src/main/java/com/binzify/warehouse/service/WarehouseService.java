package com.binzify.warehouse.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.binzify.warehouse.dto.WarehouseRequestDTO;
import com.binzify.warehouse.dto.WarehouseResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {

    WarehouseResponseDTO createWarehouse(WarehouseRequestDTO warehouseRequest);

    WarehouseResponseDTO updateWarehouse(Long warehouseId, WarehouseRequestDTO warehouseRequest);

    WarehouseResponseDTO getWarehouseById(Long warehouseId);


    void deleteWarehouse(Long id);

    // Custom business logic methods can be added here, e.g., updateCapacity
}
