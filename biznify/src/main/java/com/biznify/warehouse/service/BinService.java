package com.biznify.warehouse.service;

import com.biznify.warehouse.dto.BinDTO;
import com.biznify.warehouse.dto.BinResponseDTO;
import com.biznify.warehouse.dto.StorageRequestDTO;
import com.biznify.warehouse.entity.Bin;

import java.util.List;
import java.util.Map;

public interface BinService {
	  List<BinDTO> getAvailableBins(String warehouseCode);
	    void recalculateBinVolume(Long binId);

	    List<BinResponseDTO> findSuitableBins(StorageRequestDTO requestDTO);

}
