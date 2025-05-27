package com.biznify.warehouse.dto;

import java.time.LocalDate;

import com.biznify.warehouse.common.Auditable;

import lombok.Data;


public class ProductBatchDTO extends Auditable {
    private Long batchId;
    private Long productId;
    private Long binId;
    private String binCode;
    private double quantity;
    private LocalDate receivedDate;
    private String batchNumber;
    private LocalDate manufactureDate;
    private LocalDate expirationDate;
    private Long handledByEmployeeId;
    private Long inboundShipmentId;
}
