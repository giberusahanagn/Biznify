package com.biznify.product.dto;

import lombok.Data;

@Data
public class PartnerDTO {
    private Long id;
    private String name;
    private String email;
    private String contactNumber;
    private String status;
    private String websiteUrl;
}