package com.biznify.partner.dto;

import java.util.List;

public class PartnerWithProductsDTO {
    private PartnerDTO partner;
    private List<ProductDTO> products;

    public PartnerWithProductsDTO() {
    }

    public PartnerWithProductsDTO(PartnerDTO partner, List<ProductDTO> products) {
        this.partner = partner;
        this.products = products;
    }

    public PartnerDTO getPartner() {
        return partner;
    }

    public void setPartner(PartnerDTO partner) {
        this.partner = partner;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
