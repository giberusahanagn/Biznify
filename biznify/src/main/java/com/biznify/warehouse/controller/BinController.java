package com.biznify.warehouse.controller;

import com.biznify.warehouse.dto.AllocationRequestDTO;
import com.biznify.warehouse.dto.BinAllocationResponseDTO;
import com.biznify.warehouse.dto.BinDTO;
import com.biznify.warehouse.dto.BinResponseDTO;
import com.biznify.warehouse.dto.InboundShipmentDTO;
import com.biznify.warehouse.dto.StorageRequestDTO;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.exception.ResourceNotFoundException;
import com.biznify.warehouse.service.BinService;
import com.biznify.warehouse.service.InboundShipmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bin")
public class BinController {

	@Autowired
	private BinService binService;

	@Autowired
	private InboundShipmentService inboundShipmentService;

	@GetMapping("/available")
	public List<BinDTO> getAvailableBins(@RequestParam String warehouseCode) {
		return binService.getAvailableBins(warehouseCode);
	}

	@PostMapping("/find-available")
	public List<BinResponseDTO> findAvailableBins(@RequestBody StorageRequestDTO requestDTO) {
		System.out.println("Wareh	ouse Code: " + requestDTO.getWarehouseCode());
		List<BinResponseDTO> result = binService.findSuitableBins(requestDTO);
		System.out.println("Bins found: " + result.size());
		return result;
	}

	@PostMapping("/allocate")
	public ResponseEntity<List<BinAllocationResponseDTO>> allocateBins(@RequestBody AllocationRequestDTO request) {
		List<BinAllocationResponseDTO> result = binService.allocateProductToBins(request.getWarehouseCode(),
				request.getProductId(), request.getTotalUnits());
		return ResponseEntity.ok(result);
	}

	@PostMapping("/inboundShipment/create")
	public ResponseEntity<InboundShipmentDTO> createInboundShipment(@RequestBody InboundShipmentDTO dto) {
		return ResponseEntity.ok(inboundShipmentService.createInboundShipment(dto));
	}

}