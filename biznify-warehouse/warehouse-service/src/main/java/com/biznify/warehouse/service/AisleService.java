package com.biznify.warehouse.service;

import java.util.List;

import com.biznify.warehouse.dto.AisleRequestDTO;
import com.biznify.warehouse.dto.AisleResponseDTO;

public interface AisleService {

    AisleResponseDTO createAisle(AisleRequestDTO dto);

    AisleResponseDTO updateAisle(Long id, AisleRequestDTO dto);

    AisleResponseDTO getAisleById(Long id);

    List<AisleResponseDTO> getAislesByWarehouseId(Long warehouseId);

    void deleteAisle(Long id);
}
