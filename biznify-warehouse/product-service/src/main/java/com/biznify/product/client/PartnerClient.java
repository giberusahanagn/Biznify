package com.biznify.product.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.biznify.product.dto.PartnerDTO;

@Component
public class PartnerClient {

	@Autowired
    private WebClient.Builder webClientBuilder;
	 
	
	 public List<PartnerDTO> getProductsByPartnerId(Long partnerId) {
	        return webClientBuilder.build()
	            .get()
	            .uri("http://partner-service/api/partners/products/" + partnerId)
	            .retrieve()
	            .bodyToFlux(PartnerDTO.class)
	            .collectList()
	            .block(); // Blocking for simplicity in synchronous app
	    }
	 
	 
	 
	
}
