package com.biznify.warehouse.dto;


import lombok.Data;

@Data
public class StorageRequestDTO {
    private Long productId;
    private Integer quantity;
}