
package com.biznify.inbound.controller;

import com.biznify.inbound.dto.InboundShipmentRequestDTO;
import com.biznify.inbound.service.InboundShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inbound-shipments")
public class InboundShipmentController {

    @Autowired
    private InboundShipmentService inboundShipmentService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createInboundShipment(@RequestBody InboundShipmentRequestDTO requestDTO) {
        inboundShipmentService.processInboundShipment(requestDTO);
        return ResponseEntity.ok("Inbound shipment processed successfully.");
    }
}
