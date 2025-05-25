package com.biznify.warehouse.service;

import java.util.List;

import com.biznify.warehouse.dto.InboundShipmentDTO;


public interface InboundShipmentService {
	   InboundShipmentDTO createInboundShipment(InboundShipmentDTO dto);
	    List<InboundShipmentDTO> getAllInboundShipments();
}
