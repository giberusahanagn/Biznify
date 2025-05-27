package com.biznify.warehouse.controller;

import com.biznify.warehouse.dto.AisleDTO;
import com.biznify.warehouse.service.AisleService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aisles")

public class AisleController {
	@Autowired
	private AisleService aisleService;

	@GetMapping("/by-warehouse")
	public List<AisleDTO> getAislesByWarehouse(@RequestParam String warehouseCode) {
		return aisleService.getAislesByWarehouseCode(warehouseCode);
	}
}
