package com.biznify.inbound.client;

import com.biznify.inbound.dto.ProductBatchRequestDTO;
import com.biznify.inbound.dto.ProductBatchResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProductBatchClient {

    private final WebClient webClient = WebClient.create("http://localhost:8084");

    public ProductBatchResponseDTO sendBatchToProductService(ProductBatchRequestDTO batch) {
        try {
            return webClient.post()
                    .uri("/api/product-batches/store")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(batch)
                    .retrieve()
                    .onStatus(
                            status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> response.bodyToMono(String.class)
                                    .flatMap(body -> {
                                        System.err.println("Failed to store batch: " + body);
                                        return Mono.error(new RuntimeException("Failed to store batch: " + body));
                                    })
                    )
                    .bodyToMono(ProductBatchResponseDTO.class)
                    .block(); // This returns the actual DTO now
        } catch (Exception e) {
            System.err.println("WebClient error: " + e.getMessage());
            throw new RuntimeException("WebClient failed", e);
        }
    }
}
