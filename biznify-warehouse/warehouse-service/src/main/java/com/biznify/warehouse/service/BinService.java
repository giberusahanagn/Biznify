package com.biznify.warehouse.service;

import java.util.List;

import com.biznify.warehouse.dto.BinAllocationRequestDTO;
import com.biznify.warehouse.dto.BinLocationResponseDTO;
import com.biznify.warehouse.dto.BinRequestDTO;
import com.biznify.warehouse.dto.BinResponseDTO;

public interface BinService {
	BinResponseDTO createBin(BinRequestDTO dto);

	BinResponseDTO updateBin(Long binId, BinRequestDTO dto);

	BinResponseDTO getBinById(Long binId);

	List<BinResponseDTO> getBinsByRackId(Long rackId);

	void deleteBin(Long binId);

	List<BinLocationResponseDTO> allocateBins(BinAllocationRequestDTO request);

}