package com.biznify.warehouse.service;


import org.springframework.stereotype.Service;

import com.biznify.warehouse.dto.WarehouseDTO;
import com.biznify.warehouse.entity.Warehouse;

@Service
public interface WarehouseService {

    WarehouseDTO getFullWarehouseStructure(String warehouseCode);

    WarehouseDTO createWarehouseWithStructure(WarehouseDTO warehouseDTO);
}
