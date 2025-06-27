package com.biznify.partner.client;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.biznify.partner.dto.ProductDTO;

@Component
public class ProductClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<ProductDTO> getProductsByPartnerId(Long partnerId) {
        try {
            List<ProductDTO> products = webClientBuilder.build()
                .get()
                .uri("http://product-service/api/products/partner/" + partnerId)                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .collectList()
                .block();
            System.out.println("Fetched products: " + products);
            return products;
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // Rethrow to see the root cause in logs
        }
    }
}