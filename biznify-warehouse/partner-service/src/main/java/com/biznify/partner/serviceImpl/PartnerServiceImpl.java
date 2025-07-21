package com.biznify.partner.serviceImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.biznify.partner.dto.PartnerWithProductsDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biznify.partner.client.ProductClient;
import com.biznify.partner.dto.PartnerDTO;
import com.biznify.partner.dto.ProductDTO;
import com.biznify.partner.entity.Partner;
import com.biznify.partner.exception.ResourceNotFoundException;
import com.biznify.partner.repository.PartnerRepository;
import com.biznify.partner.service.PartnerService;

@Service
public class PartnerServiceImpl implements PartnerService {

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ProductClient productClient;

    @Override
    public PartnerDTO createPartner(PartnerDTO partnerDTO) {
        Partner partner = new Partner();
        BeanUtils.copyProperties(partnerDTO, partner);
        Partner saved = partnerRepository.save(partner);

        PartnerDTO response = new PartnerDTO();
        BeanUtils.copyProperties(saved, response);
        return response;
    }

    @Override
    public PartnerDTO getPartnerById(Long id) {
        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found with id: " + id));
        PartnerDTO dto = new PartnerDTO();
        BeanUtils.copyProperties(partner, dto);
        return dto;
    }

    @Override
    public List<PartnerDTO> getAllPartners() {
        return partnerRepository.findAll().stream().map(partner -> {
            PartnerDTO dto = new PartnerDTO();
            BeanUtils.copyProperties(partner, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PartnerDTO> getByName(String name) {
        List<Partner> partners = partnerRepository.findByNameContainingIgnoreCase(name);
        return partners.stream().map(partner -> {
            PartnerDTO dto = new PartnerDTO();
            BeanUtils.copyProperties(partner, dto);
            return dto;
        }).collect(Collectors.toList());
    }
    @Override
    public PartnerWithProductsDTO getPartnerWithProducts(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found with id: " + partnerId));

        PartnerDTO partnerDTO = new PartnerDTO();
        BeanUtils.copyProperties(partner, partnerDTO);

        List<ProductDTO> products = productClient.getProductsByPartnerId(partnerId);

        return new PartnerWithProductsDTO(partnerDTO, products);
    }


    public String getEmailByPartnerId(Long partnerId) {
        return partnerRepository.findById(partnerId)
                .map(Partner::getEmail)
                .orElseThrow(() -> new IllegalArgumentException("Partner not found with ID: " + partnerId));
    }
}