package com.biznify.warehouse.serviceImplimentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biznify.warehouse.entity.Partner;
import com.biznify.warehouse.exception.ResourceNotFoundException;
import com.biznify.warehouse.repository.PartnerRepository;
import com.biznify.warehouse.service.PartnerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PartnerServiceImplementation implements PartnerService {

	  @Autowired
	    private PartnerRepository partnerRepository;

	    @Override
	    public Partner createPartner(Partner partner) {
	        return partnerRepository.save(partner);
	    }

	    @Override
	    public Partner updatePartner(Long id, Partner partnerDetails) {
	        Partner partner = partnerRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Partner not found with id: " + id));
	        
	        partner.setName(partnerDetails.getName());
	        partner.setEmail(partnerDetails.getEmail());
	        partner.setContactNumber(partnerDetails.getContactNumber());
	        // Add any other fields you have in Partner entity
	        
	        return partnerRepository.save(partner);
	    }

	    @Override
	    public void deletePartner(Long id) {
	        Partner partner = partnerRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Partner not found with id: " + id));
	        partnerRepository.delete(partner);
	    }

	    @Override
	    public Partner getPartnerById(Long id) {
	        return partnerRepository.findById(id)
	                .orElseThrow(() -> new ResourceNotFoundException("Partner not found with id: " + id));
	    }

	    @Override
	    public List<Partner> getAllPartners() {
	        return partnerRepository.findAll();
	    }
}
