package com.biznify.product.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.biznify.product.dto.PartnerDTO;
import com.biznify.product.dto.PartnerWithProductsDTO;
import com.biznify.product.dto.ProductDTO;
import com.biznify.product.dto.ProductWithPartnerDTO;
import com.biznify.product.entity.Product;
import com.biznify.product.exception.ResourceNotFoundException;
import com.biznify.product.repository.ProductRepository;
import com.biznify.product.service.ProductService;

import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    private static final String PARTNER_SERVICE_BASE_URL = "http://partner-service/api/partners";

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        System.out.println("entred craete method in service Impl");

        validatePartnerExists(productDTO.getPartnerId());
        System.out.println("validating parter by id");
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        System.out.println("converting setter and getter");


        Product saved = productRepository.save(product);
        System.out.println("Called Save method");

        ProductDTO response = convertToDTO(saved);
        System.out.println("Responce" + response);

        return response;
    }

    private void validatePartnerExists(Long partnerId) {
        try {
            WebClient webClient = webClientBuilder.baseUrl("http://partner-service").build();
            webClient.get()
                    .uri("/api/partners/{id}", partnerId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new ResourceNotFoundException("Partner not found with ID: " + partnerId)))
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new ResourceNotFoundException("Error validating partner ID: " + partnerId);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to validate partner ID: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductBySku(String sku) {
        Optional<Product> productOpt = productRepository.findBySku(sku);
        if (productOpt.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with SKU: " + sku);
        }
        return convertToDTO(productOpt.get());
    }

    @Override
    public ProductWithPartnerDTO getProductWithPartner(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        ProductWithPartnerDTO dto = new ProductWithPartnerDTO();
        BeanUtils.copyProperties(product, dto);

        if (product.getPartnerId() != null) {
            try {
                PartnerDTO partner = webClientBuilder.baseUrl(PARTNER_SERVICE_BASE_URL).build()
                        .get()
                        .uri("/{id}", product.getPartnerId())
                        .retrieve()
                        .bodyToMono(PartnerDTO.class)
                        .block();
                dto.setPartner(partner);
            } catch (WebClientResponseException ex) {
                if (ex.getStatusCode().is4xxClientError()) {
                    dto.setPartner(null);
                } else {
                    throw new RuntimeException("Partner Service Error: " + ex.getMessage(), ex);
                }
            } catch (Exception e) {
                dto.setPartner(null);
            }
        }

        return dto;
    }

    @Override
    public PartnerWithProductsDTO getPartnerWithProducts(Long partnerId) {
        // Call Partner-Service using WebClient
        PartnerDTO partnerDTO = null;
        try {
            partnerDTO = webClientBuilder.baseUrl(PARTNER_SERVICE_BASE_URL).build()
                    .get()
                    .uri("/{id}", partnerId)
                    .retrieve()
                    .bodyToMono(PartnerDTO.class)
                    .block();
        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode().is4xxClientError()) {
                throw new ResourceNotFoundException("Partner not found with id: " + partnerId);
            } else {
                throw new RuntimeException("Partner Service Error: " + ex.getMessage(), ex);
            }
        }

        // Fetch all products linked to the partner
        List<ProductDTO> productList = getByPartnerId(partnerId);


        return new PartnerWithProductsDTO(partnerDTO, productList);
    }

    @Override
    public List<ProductDTO> getByPartnerId(Long partnerId) {
        System.out.println("🔍 Fetching products for partnerId: " + partnerId);
        List<Product> products = productRepository.findByPartnerId(partnerId);
        System.out.println("✅ Found products: " + products.size());
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


    private ProductDTO convertToDTO(Product products) {
        ProductDTO dto = new ProductDTO();
        try {
            BeanUtils.copyProperties(products, dto);

            if (products.getCreatedAt() != null) {
                dto.setCreatedAt(products.getCreatedAt().toString());
            }

            if (products.getExpiryDate() != null) {
                dto.setExpiryDate(products.getExpiryDate().toString());
            }

            if (products.getUpdatedAt() != null) {
                dto.setUpdatedAt(products.getUpdatedAt().toString());
            }

            dto.setPartnerId(products.getPartnerId());
            dto.setTemperatureSensitive(products.getTemperatureSensitive());
        } catch (Exception e) {
            System.err.println("❌ Error converting product to DTO: " + e.getMessage());
            e.printStackTrace();
        }

        return dto;
    }

}
