package com.biznify.warehouse.dto;

import lombok.Data;

@Data
public class DeliveryPartnerDTO {
    private Long id;
    private String name;
    private String email;
    private String contactNumber;
    private String registrationNumber;
    private String address;
}