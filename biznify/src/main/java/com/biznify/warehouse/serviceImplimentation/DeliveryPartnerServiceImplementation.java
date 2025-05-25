package com.biznify.warehouse.serviceImplimentation;

import com.biznify.warehouse.dto.DeliveryPartnerDTO;
import com.biznify.warehouse.entity.DeliveryPartner;
import com.biznify.warehouse.repository.DeliveryPartnerRepository;
import com.biznify.warehouse.service.DeliveryPartnerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryPartnerServiceImplementation implements DeliveryPartnerService {

    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @Override
    public DeliveryPartner addDeliveryPartner(DeliveryPartnerDTO dto) {
        DeliveryPartner partner = new DeliveryPartner();
        BeanUtils.copyProperties(dto, partner);
        return deliveryPartnerRepository.save(partner);
    }

    @Override
    public List<DeliveryPartner> getAllDeliveryPartners() {
        return deliveryPartnerRepository.findAll();
    }

    @Override
    public DeliveryPartner getDeliveryPartnerById(Long id) {
        return deliveryPartnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery Partner not found"));
    }
}
