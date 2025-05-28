package com.biznify.warehouse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.biznify.warehouse.dto.InboundShipmentDTO;
import com.biznify.warehouse.exception.InsufficientSpaceException;

@Service
public interface InboundShipmentService {
	   InboundShipmentDTO createInboundShipment(InboundShipmentDTO dto) throws InsufficientSpaceException;
	    List<InboundShipmentDTO> getAllInboundShipments();
}
