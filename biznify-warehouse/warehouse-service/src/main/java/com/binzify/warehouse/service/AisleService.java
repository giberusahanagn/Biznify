package com.binzify.warehouse.service;

import java.util.List;

import com.binzify.warehouse.dto.AisleRequestDTO;
import com.binzify.warehouse.dto.AisleResponseDTO;

public interface AisleService {

    AisleResponseDTO createAisle(AisleRequestDTO dto);

    AisleResponseDTO updateAisle(Long id, AisleRequestDTO dto);

    AisleResponseDTO getAisleById(Long id);

    List<AisleResponseDTO> getAislesByWarehouseId(Long warehouseId);

    void deleteAisle(Long id);
}
