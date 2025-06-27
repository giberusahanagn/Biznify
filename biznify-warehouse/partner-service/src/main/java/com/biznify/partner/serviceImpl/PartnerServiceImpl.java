package com.biznify.partner.serviceImpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Map<String, Object> getPartnerWithProducts(Long partnerId) {
        Partner partner = partnerRepository.findById(partnerId).orElse(null);
        if (partner == null)
            return null;
        PartnerDTO partnerDTO = new PartnerDTO();
        BeanUtils.copyProperties(partner, partnerDTO);

        // THIS LINE: should return a list of real ProductDTOs
        List<ProductDTO> products = productClient.getProductsByPartnerId(partnerId);
System.out.println(products.toString());
        Map<String, Object> response = new HashMap<>();
        response.put("partner", partnerDTO);
        response.put("products", products);
        return response;
    }
}