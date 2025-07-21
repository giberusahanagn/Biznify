package com.biznify.partner.service;

import java.util.List;
import java.util.Map;

import com.biznify.partner.dto.PartnerDTO;
import com.biznify.partner.dto.PartnerWithProductsDTO;

public interface PartnerService {
    PartnerDTO createPartner(PartnerDTO partnerDTO);

    PartnerDTO getPartnerById(Long id);

    List<PartnerDTO> getAllPartners();

 //   List<ProductDTO> getProductsByPartner(Long partnerId);

    List<PartnerDTO> getByName(String name);

    PartnerWithProductsDTO getPartnerWithProducts(Long partnerId);

     String getEmailByPartnerId(Long partnerId) ;
}