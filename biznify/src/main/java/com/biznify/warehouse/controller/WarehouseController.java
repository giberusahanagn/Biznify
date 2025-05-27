package com.biznify.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.biznify.warehouse.dto.WarehouseDTO;
import com.biznify.warehouse.entity.Warehouse;
import com.biznify.warehouse.service.WarehouseService;

@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

	@Autowired
	private WarehouseService warehouseService;

	@GetMapping("/{code}/structure")
	public WarehouseDTO getWarehouseStructure(@PathVariable String code) {

		return warehouseService.getFullWarehouseStructure(code);
	}
	  @PostMapping("/createWareHouse")
	    public ResponseEntity<WarehouseDTO> createWarehouseWithStructure(@RequestBody WarehouseDTO warehouseDTO) {
	        WarehouseDTO createdWarehouse = warehouseService.createWarehouseWithStructure(warehouseDTO);
	        return new ResponseEntity<>(createdWarehouse, HttpStatus.CREATED);
	    }

}