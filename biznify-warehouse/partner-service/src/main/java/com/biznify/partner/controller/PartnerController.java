package com.biznify.partner.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biznify.partner.dto.PartnerDTO;
import com.biznify.partner.dto.ProductDTO;
import com.biznify.partner.service.PartnerService;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

	@Autowired
	private PartnerService partnerService;


	@PostMapping
	public PartnerDTO create(@RequestBody PartnerDTO dto) {
		return partnerService.createPartner(dto);
	}

	// Controller
	@GetMapping("/{id}")
	public ResponseEntity<PartnerDTO> getPartnerById(@PathVariable Long id) {
	    PartnerDTO dto = partnerService.getPartnerById(id);
	    return ResponseEntity.ok(dto);
	}


	@GetMapping
	public List<PartnerDTO> getAll() {
		return partnerService.getAllPartners();
	}

	@GetMapping("/name/{name}")
	public List<PartnerDTO> getByName(@PathVariable String name) {
	    return partnerService.getByName(name);
	}
	
	   @GetMapping("/{id}/products")
	    public ResponseEntity<?> getProductsByPartner(@PathVariable Long partnerId) {
	        Map<String, Object> response = partnerService.getPartnerWithProducts(partnerId);
	        if (response == null) {
	            return ResponseEntity.notFound().build();
	        }
	        return ResponseEntity.ok(response);
	    }
}
