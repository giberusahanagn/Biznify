package com.binzify.productBatch.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.binzify.productBatch.dto.*;
import com.binzify.productBatch.entity.ProductBatch;
import com.binzify.productBatch.enums.ProductBatchStatus;
import com.binzify.productBatch.exceptions.BinAllocationException;
import com.binzify.productBatch.exceptions.ResourceNotFoundException;
import com.binzify.productBatch.repository.ProductBatchRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Slf4j
@Service

public class ProductBatchServiceImpl implements ProductBatchService {

	@Autowired
    private  ProductBatchRepository productBatchRepository;
	@Autowired
    private  WebClient.Builder webClientBuilder;

    @Autowired
    private MailServiceImpl mailService;

    private static final String PRODUCT_SERVICE_BASE_URL = "http://localhost:8082/api/products/sku/";
    private static final String PARTNER_SERVICE_BASE_URL = "http://localhost:8081/api/partners/";
    private static final String WAREHOUSE_SERVICE_BASE_URL = "http://localhost:8083/api/warehouses";
    private static final String BIN_SERVICE_BASE_URL = "http://localhost:8083/api/bins";

    @Override
    public ProductBatchResponseDTO createBatch(ProductBatchRequestDTO dto) {
        validateProductPartnerWarehouse(dto);

        if (productBatchRepository.existsByBatchCode(dto.getBatchCode())) {
            throw new IllegalArgumentException("Batch code already exists");
        }
        if (productBatchRepository.existsBySkuCode(dto.getSkuCode())) {
            throw new IllegalArgumentException("SKU code already exists");
        }

        ProductDTO product = fetchProductBySku(dto.getSkuCode());

        ProductBatch batch = new ProductBatch();
        BeanUtils.copyProperties(dto, batch);
        batch.setProductId(product.getId());
        batch.setPartnerId(product.getPartnerId());
        batch.setCreatedAt(LocalDateTime.now());
        batch.setUpdatedAt(LocalDateTime.now());
        batch.setStatus(ProductBatchStatus.ACTIVE);

        return convertToDTO(productBatchRepository.save(batch));
    }

    @Override
    public ProductBatchResponseDTO storeProductBatch(ProductBatchRequestDTO dto) {
        validateProductPartnerWarehouse(dto);

        ProductDTO product = fetchProductBySku(dto.getSkuCode());

        double totalVolume = safeDouble(dto.getQuantity()) *
                safeDouble(product.getLengthCm()) *
                safeDouble(product.getWidthCm()) *
                safeDouble(product.getHeightCm());
        log.info("Total volume to store: {}", totalVolume);

        BinAllocationRequestDTO binRequest = new BinAllocationRequestDTO();
        binRequest.setLengthCm(product.getLengthCm());
        binRequest.setWidthCm(product.getWidthCm());
        binRequest.setHeightCm(product.getHeightCm());
        binRequest.setUnitsToStore(dto.getQuantity());
        binRequest.setWarehouseId(dto.getWarehouseId());
        binRequest.setSkuCode(dto.getSkuCode());

        List<BinLocationResponseDTO> bins = allocateBins(binRequest);
        if (bins == null || bins.isEmpty()) {
            throw new BinAllocationException("No bins allocated for the product batch", null);
        }

        double remainingUnits = dto.getQuantity();
        List<ProductBatch> batchesToSave = new ArrayList<>();
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long baseSequence = productBatchRepository.count();
        ProductBatch tempBatch = null;
        for (BinLocationResponseDTO bin : bins) {
            if (remainingUnits <= 0) break;

            double binCapacityUnits = bin.getUnitsAllocated(); // Use allocated units directly
            double unitsToStore = Math.min(remainingUnits, binCapacityUnits);
            ProductBatch batch = new ProductBatch();
            BeanUtils.copyProperties(dto, batch);
            batch.setStatus(ProductBatchStatus.STORED);
            batch.setCreatedAt(LocalDateTime.now());
            batch.setUpdatedAt(LocalDateTime.now());

            batch.setBinCode(bin.getBinCode());
            batch.setBinId(bin.getBinId());
            batch.setWarehouseId(dto.getWarehouseId());
            batch.setQuantity(unitsToStore);

            long sequence = baseSequence + batchesToSave.size() + 1;
            batch.setBatchCode("BC-" + today + "-" + sequence);
            batch.setInvoiceNumber("INV-" + today + "-" + sequence);

            batch.setLengthCm(product.getLengthCm());
            batch.setWidthCm(product.getWidthCm());
            batch.setHeightCm(product.getHeightCm());
            batch.setProductId(product.getId());
            batch.setPartnerId(product.getPartnerId());

            batchesToSave.add(batch);
            remainingUnits -= unitsToStore;
            tempBatch = batch;
            log.info("Stored {} units in batch {}, remaining units: {}", unitsToStore, batch.getBatchCode(), remainingUnits);
        }

        if (remainingUnits > 0) {
            throw new BinAllocationException("Unable to allocate all units. Units left: " + remainingUnits, null);
        }

        List<ProductBatch> savedBatches = productBatchRepository.saveAll(batchesToSave);

     // Fetch the latest partner info (including email) from partner-service
        PartnerDTO partner = fetchPartnerFromService(dto.getPartnerId());
        log.info("Sending email for batch: {} to partner: {}", tempBatch.getBatchCode(), partner.getEmail());
        // Send notification email to the partner for each stored batch
        savedBatches.forEach(batch -> mailService.sendPartnerBatchStoredEmail(batch, partner));
      

        return convertToDTO(savedBatches.get(0));
    }

    @Override
    public List<ProductBatchResponseDTO> getAll() {
        return productBatchRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public ProductBatchResponseDTO getById(Long id) {
        ProductBatch batch = productBatchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + id));
        return convertToDTO(batch);
    }

    @Override
    public ProductBatchResponseDTO updateStatus(Long id, String status) {
        ProductBatch batch = productBatchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product batch not found with id: " + id));

        try {
            batch.setStatus(ProductBatchStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        batch.setUpdatedAt(LocalDateTime.now());
        return convertToDTO(productBatchRepository.save(batch));
    }

    @Override
    public ProductBatch updateBatchQuantity(Long batchId, Double quantity) {
        ProductBatch batch = productBatchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found with id: " + batchId));
        batch.setQuantity(batch.getQuantity() + quantity);
        batch.setUpdatedAt(LocalDateTime.now());
        return productBatchRepository.save(batch);
    }

    @Override
    public List<ProductBatch> getBatchesByWarehouse(Long warehouseId) {
        return productBatchRepository.getByWarehouseId(warehouseId);
    }

    // ──────── VALIDATION & EXTERNAL CALLS ────────

    private void validateProductPartnerWarehouse(ProductBatchRequestDTO dto) {
        if (dto.getWarehouseId() == null) {
            throw new IllegalArgumentException("Warehouse ID is required");
        }
        validateWarehouse(dto.getWarehouseId());

        ProductDTO product = fetchProductBySku(dto.getSkuCode());
        validatePartner(product.getPartnerId());
    }

    private ProductDTO fetchProductBySku(String skuCode) {
        try {
            return webClientBuilder.baseUrl(PRODUCT_SERVICE_BASE_URL).build()
                    .get().uri("/{sku}", skuCode)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            response -> Mono.error(new ResourceNotFoundException("Product not found with SKU: " + skuCode)))
                    .bodyToMono(ProductDTO.class).block();
        } catch (WebClientResponseException e) {
            throw new ResourceNotFoundException("Error fetching product: " + e.getStatusCode());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Unexpected error fetching product: " + e.getMessage());
        }
    }

    private void validatePartner(Long partnerId) {
        try {
            webClientBuilder.baseUrl(PARTNER_SERVICE_BASE_URL).build()
                    .get().uri("/{id}", partnerId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            response -> Mono.error(new ResourceNotFoundException("Partner not found: " + partnerId)))
                    .toBodilessEntity().block();
        } catch (WebClientResponseException e) {
            throw new ResourceNotFoundException("Failed to validate partner ID: " + e.getStatusCode());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Unexpected error during partner validation: " + e.getMessage());
        }
    }

    private void validateWarehouse(Long warehouseId) {
        try {
            webClientBuilder.baseUrl(WAREHOUSE_SERVICE_BASE_URL).build()
                    .get().uri("/{id}", warehouseId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            response -> Mono.error(new ResourceNotFoundException("Warehouse not found: " + warehouseId)))
                    .onStatus(HttpStatusCode::is5xxServerError,
                            response -> Mono.error(new RuntimeException("Warehouse service error (500)")))
                    .toBodilessEntity().block();
        } catch (WebClientResponseException e) {
            throw new ResourceNotFoundException("Failed to validate warehouse ID: " + e.getStatusCode());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Unexpected error during warehouse validation: " + e.getMessage());
        }
    }

    private List<BinLocationResponseDTO> allocateBins(BinAllocationRequestDTO request) {
        try {
            return webClientBuilder.build()
                    .post()
                    .uri(BIN_SERVICE_BASE_URL + "/allocate")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<BinLocationResponseDTO>>() {})
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                            .filter(throwable -> throwable instanceof WebClientResponseException))
                    .doOnError(WebClientResponseException.class, ex ->
                            log.error("Error from bin allocation service: {}", ex.getResponseBodyAsString()))
                    .block();
        } catch (Exception ex) {
            throw new BinAllocationException("Failed to allocate bins after retries", ex);
        }
    }

    private ProductBatchResponseDTO convertToDTO(ProductBatch batch) {
        ProductBatchResponseDTO dto = new ProductBatchResponseDTO();
        BeanUtils.copyProperties(batch, dto);
        dto.setStatus(batch.getStatus());
        return dto;
    }

    private double safeDouble(Double value) {
        return value != null ? value : 0.0;
    }

    /**
     * Fetches the latest partner details (including email) from partner-service.
     */
    private PartnerDTO fetchPartnerFromService(Long partnerId) {
        try {
            return webClientBuilder.baseUrl(PARTNER_SERVICE_BASE_URL).build()
                    .get().uri("/{id}", partnerId)
                    .retrieve()
                    .bodyToMono(PartnerDTO.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch partner info", e);
        }
    }

} 