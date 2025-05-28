package com.biznify.warehouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biznify.warehouse.dto.BinAllocationResponseDTO;
import com.biznify.warehouse.dto.InboundShipmentDTO;
import com.biznify.warehouse.exception.InsufficientSpaceException;
import com.biznify.warehouse.service.InboundShipmentService;
import com.biznify.warehouse.serviceImplimentation.BinAllocationService;

@RestController
@RequestMapping("/api/inbound-shipments")
public class InboundShipmentController {

	@Autowired
	private InboundShipmentService inboundShipmentService;

	@Autowired
	private BinAllocationService binAllocationService;

	@PostMapping
	public ResponseEntity<InboundShipmentDTO> createInboundShipment(@RequestBody InboundShipmentDTO dto) throws InsufficientSpaceException {
		InboundShipmentDTO saved = inboundShipmentService.createInboundShipment(dto);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<InboundShipmentDTO>> getAllInboundShipments() {
		return ResponseEntity.ok(inboundShipmentService.getAllInboundShipments());
	}
}
