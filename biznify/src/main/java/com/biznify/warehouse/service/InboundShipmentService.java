package com.biznify.warehouse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.biznify.warehouse.dto.InboundShipmentDTO;

@Service
public interface InboundShipmentService {
	   InboundShipmentDTO createInboundShipment(InboundShipmentDTO dto);
	    List<InboundShipmentDTO> getAllInboundShipments();
}
