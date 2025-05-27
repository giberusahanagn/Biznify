package com.biznify.warehouse.service;

import com.biznify.warehouse.dto.AisleDTO;

import java.util.List;

import org.springframework.stereotype.Service;

public interface AisleService {
    List<AisleDTO> getAislesByWarehouseCode(String warehouseCode);
}
