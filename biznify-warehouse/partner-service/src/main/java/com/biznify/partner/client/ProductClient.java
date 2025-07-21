package com.biznify.partner.client;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.biznify.partner.dto.ProductDTO;

@Component
public class ProductClient {

    private final WebClient webClient;

    @Autowired
    public ProductClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build(); // Should be @LoadBalanced
    }

    public List<ProductDTO> getProductsByPartnerId(Long partnerId) {
        try {
            return webClient.get()
                    .uri("http://product-service/api/products/partner/{id}", partnerId)
                    .retrieve()
                    .bodyToFlux(ProductDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.err.println("Failed to fetch products for partnerId=" + partnerId + ": " + e.getMessage());
            return Collections.emptyList(); // Optional fallback
        }
    }
}
