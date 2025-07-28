
package com.biznify.inbound.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Data
public class InboundShipmentRequestDTO {
    // Getters and setters
    private Long partnerId;

    private List<ProductBatchRequestDTO> productBatches;


}
