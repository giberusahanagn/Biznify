package com.biznify.warehouse.service;

import java.util.List;

import com.biznify.warehouse.dto.BinAllocationResponseDTO;

public interface AllocationService {
    List<BinAllocationResponseDTO> allocateProductToBins(String warehouseCode, Long productId, int totalUnits);
}
