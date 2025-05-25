package com.biznify.warehouse.service;

import com.biznify.warehouse.dto.DeliveryPartnerDTO;
import com.biznify.warehouse.entity.DeliveryPartner;

import java.util.List;

public interface DeliveryPartnerService {
    DeliveryPartner addDeliveryPartner(DeliveryPartnerDTO dto);
    List<DeliveryPartner> getAllDeliveryPartners();
    DeliveryPartner getDeliveryPartnerById(Long id);
}
