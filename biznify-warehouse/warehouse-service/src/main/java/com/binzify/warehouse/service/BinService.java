package com.binzify.warehouse.service;

import java.util.List;

import com.binzify.warehouse.dto.BinAllocationRequestDTO;
import com.binzify.warehouse.dto.BinLocationResponseDTO;
import com.binzify.warehouse.dto.BinRequestDTO;
import com.binzify.warehouse.dto.BinResponseDTO;

public interface BinService {
	BinResponseDTO createBin(BinRequestDTO dto);

	BinResponseDTO updateBin(Long binId, BinRequestDTO dto);

	BinResponseDTO getBinById(Long binId);

	List<BinResponseDTO> getBinsByRackId(Long rackId);

	void deleteBin(Long binId);

	List<BinLocationResponseDTO> allocateBins(BinAllocationRequestDTO request);

}