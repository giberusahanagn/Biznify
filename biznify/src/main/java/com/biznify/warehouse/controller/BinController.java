package com.biznify.warehouse.controller;

import com.biznify.warehouse.dto.BinDTO;
import com.biznify.warehouse.dto.BinResponseDTO;
import com.biznify.warehouse.dto.StorageRequestDTO;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.exception.ResourceNotFoundException;
import com.biznify.warehouse.service.BinService;

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
	private  BinService binService;

	@GetMapping("/available")
	public List<BinDTO> getAvailableBins(@RequestParam String warehouseCode) {
		return binService.getAvailableBins(warehouseCode);
	}

	@PostMapping("/{id}/recalculate")
	public void recalculateBinVolume(@PathVariable Long id) {
		binService.recalculateBinVolume(id);
	}
	  @PostMapping("/find-available")
	    public List<BinResponseDTO> findAvailableBins(@RequestBody StorageRequestDTO requestDTO) {
	        return binService.findSuitableBins(requestDTO);
	    }
}