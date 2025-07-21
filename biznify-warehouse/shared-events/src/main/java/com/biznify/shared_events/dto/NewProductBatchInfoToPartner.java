package com.biznify.shared_events.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewProductBatchInfoToPartner implements Serializable {

        private Long batchId;
        private String batchCode;
        private String invoiceNumber;
        private String skuCode;
        private Double quantity;
        private Long partnerId;
        private Long warehouseId;
        private LocalDateTime timestamp;

}