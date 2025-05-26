package com.biznify.warehouse.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class InboundShipmentDTO {
    private Long shipmentId;
    private LocalDateTime arrivalDate;
    private String invoiceNumber;
    private String referenceNumber;
    private String status;
    private String deliveryContactName;
    private String transportDetails;
    private Long partnerId;
    private Long deliveryPartnerId;
    private Long warehouseId;
    private Long receivedByEmployeeId;
    private String remarks;
    private List<ProductBatchDTO> productBatches;
}
