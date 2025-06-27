package com.biznify.partner.service;

import java.util.List;
import java.util.Map;

import com.biznify.partner.dto.PartnerDTO;

public interface PartnerService {
    PartnerDTO createPartner(PartnerDTO partnerDTO);

    PartnerDTO getPartnerById(Long id);

    List<PartnerDTO> getAllPartners();

 //   List<ProductDTO> getProductsByPartner(Long partnerId);

    List<PartnerDTO> getByName(String name);

	Map<String, Object> getPartnerWithProducts(Long partnerId);
}