package com.biznify.warehouse.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProductBatchDTO {
    private Long id;
    private Long productId;
    private Long binId;
    private double quantity;
    private LocalDate receivedDate;
    private String batchNumber;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    private Long handledByEmployeeId;
    private Long inboundShipmentId;
}
