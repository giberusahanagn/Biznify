package com.biznify.warehouse.service;

import com.biznify.warehouse.dto.BinAllocationResponseDTO;
import com.biznify.warehouse.dto.BinDTO;
import com.biznify.warehouse.dto.BinResponseDTO;
import com.biznify.warehouse.dto.StorageRequestDTO;

import java.util.List;

public interface BinService {

    List<BinDTO> getAvailableBins(String warehouseCode);

    List<BinResponseDTO> findSuitableBins(StorageRequestDTO requestDTO);

	//List<BinAllocationResponseDTO> allocateProductToBins(String warehouseCode, int totalUnitsToAllocate);

	List<BinAllocationResponseDTO> allocateProductToBins(String warehouseCode, Long productId,
			int totalUnitsToAllocate);

}
