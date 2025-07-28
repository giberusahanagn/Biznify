package com.biznify.inbound.service;

import com.biznify.inbound.client.ProductBatchClient;
import com.biznify.inbound.dto.InboundShipmentRequestDTO;
import com.biznify.inbound.dto.ProductBatchRequestDTO;
import com.biznify.inbound.dto.ProductBatchResponseDTO;
import com.biznify.inbound.entity.InboundShipment;
import com.biznify.inbound.repository.InboundShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class InboundShipmentServiceImpl implements InboundShipmentService {

    @Autowired
    private InboundShipmentRepository inboundShipmentRepository;

    @Autowired
    private ProductBatchClient productBatchClient;

    @Override
    public void processInboundShipment(InboundShipmentRequestDTO requestDTO) {
        InboundShipment shipment = new InboundShipment();
        shipment.setPartnerId(requestDTO.getPartnerId());
        shipment.setShipmentDate(LocalDateTime.now());

        InboundShipment savedShipment = inboundShipmentRepository.save(shipment);

        // Get invoice from first batch response
        for (int i = 0; i < requestDTO.getProductBatches().size(); i++) {
            ProductBatchRequestDTO batch = requestDTO.getProductBatches().get(i);
            batch.setInboundShipmentId(savedShipment.getId());

            ProductBatchResponseDTO response = productBatchClient.sendBatchToProductService(batch);

            // Save invoice number from the first batch only
            if (i == 0 && response.getInvoiceNumber() != null) {
                savedShipment.setInvoiceNumber(response.getInvoiceNumber());
                inboundShipmentRepository.save(savedShipment);  // update after invoice
            }
        }
    }

}
