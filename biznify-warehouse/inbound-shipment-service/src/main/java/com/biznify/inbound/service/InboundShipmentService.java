
package com.biznify.inbound.service;

import com.biznify.inbound.dto.InboundShipmentRequestDTO;

public interface InboundShipmentService {
    void processInboundShipment(InboundShipmentRequestDTO requestDTO);
}
