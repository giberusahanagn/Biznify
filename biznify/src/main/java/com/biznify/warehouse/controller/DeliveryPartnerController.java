package com.biznify.warehouse.controller;

import com.biznify.warehouse.dto.DeliveryPartnerDTO;
import com.biznify.warehouse.entity.DeliveryPartner;
import com.biznify.warehouse.service.DeliveryPartnerService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-partners")
public class DeliveryPartnerController {

    @Autowired
    private DeliveryPartnerService deliveryPartnerService;

    @PostMapping
    public ResponseEntity<DeliveryPartner> create(@RequestBody DeliveryPartnerDTO dto) {
        return ResponseEntity.ok(deliveryPartnerService.addDeliveryPartner(dto));
    }

   

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryPartner> getById(@PathVariable Long id) {
    	 DeliveryPartner partner = deliveryPartnerService.getDeliveryPartnerById(id);
    	    
    	    DeliveryPartnerDTO dto = new DeliveryPartnerDTO();
    	    BeanUtils.copyProperties(partner, dto);
   
        return ResponseEntity.ok(deliveryPartnerService.getDeliveryPartnerById(id));
    }
}
