package com.biznify.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentInventoryDTO {
    private Long productId;
    private Long binId;
    private Long rackId;
    private Long warehouseId;
    private Long netQuantity;  // sum of IN - OUT quantities
}
