package com.biznify.warehouse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biznify.warehouse.dto.InboundShipmentDTO;
import com.biznify.warehouse.service.InboundShipmentService;

@RestController
@RequestMapping("/api/inbound-shipments")
public class InboundShipmentController {

	@Autowired
	private InboundShipmentService inboundShipmentService;

	@PostMapping
	public ResponseEntity<InboundShipmentDTO> createInboundShipment(@RequestBody InboundShipmentDTO dto) {
		InboundShipmentDTO saved = inboundShipmentService.createInboundShipment(dto);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<InboundShipmentDTO>> getAllInboundShipments() {
		return ResponseEntity.ok(inboundShipmentService.getAllInboundShipments());
	}
}
