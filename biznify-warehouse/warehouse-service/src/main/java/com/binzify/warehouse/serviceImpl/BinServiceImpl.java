package com.binzify.warehouse.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.binzify.warehouse.dto.BinAllocationRequestDTO;
import com.binzify.warehouse.dto.BinLocationResponseDTO;
import com.binzify.warehouse.dto.BinRequestDTO;
import com.binzify.warehouse.dto.BinResponseDTO;
import com.binzify.warehouse.dto.ProductDTO;
import com.binzify.warehouse.entity.Bin;
import com.binzify.warehouse.entity.Rack;
import com.binzify.warehouse.enums.BinStatus;
import com.binzify.warehouse.exception.ResourceNotFoundException;
import com.binzify.warehouse.repository.BinRepository;
import com.binzify.warehouse.repository.RackRepository;
import com.binzify.warehouse.service.BinService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class BinServiceImpl implements BinService {

    private final BinRepository binRepository;
    private final RackRepository rackRepository;
    private static final String PRODUCT_SERVICE_BASE_URL = "http://localhost:8082/api/products/";
    private final WebClient webClient;

    @Autowired
    public BinServiceImpl(BinRepository binRepository, RackRepository rackRepository, WebClient.Builder webClientBuilder) {
        this.binRepository = binRepository;
        this.rackRepository = rackRepository;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public BinResponseDTO createBin(BinRequestDTO dto) {
        Rack rack = rackRepository.findById(dto.getRackId())
            .orElseThrow(() -> new ResourceNotFoundException("Rack not found with id: " + dto.getRackId()));

        // Defensive: required fields
        if (dto.getLengthCm() == null || dto.getWidthCm() == null || dto.getHeightCm() == null) {
            throw new IllegalArgumentException("Bin dimensions (length, width, height) must not be null.");
        }
        if (dto.getBinSize() == null) {
            throw new IllegalArgumentException("Bin size must not be null.");
        }

        double calculatedVolume = dto.getLengthCm() * dto.getWidthCm() * dto.getHeightCm();

        Bin bin = new Bin();
        BeanUtils.copyProperties(dto, bin); // This may set volumes/usedVolume to null, so always override
        bin.setRack(rack);

        // Always set all not-null fields!
        bin.setVolumeCapacity(calculatedVolume);
        bin.setAvailableVolume(calculatedVolume);
        bin.setUsedVolume(0.0);
        bin.setCurrentUnitQuantity(0.0);
        bin.setMaxUnitCapacity(dto.getMaxUnitCapacity() != null ? dto.getMaxUnitCapacity() : 0.0);
        bin.setStatus(BinStatus.EMPTY);
        bin.setOccupied(false);
        if (dto.getProductId() != null) bin.setProductId(dto.getProductId());

        Bin saved = binRepository.save(bin);
        return convertToDto(saved);
    }

    @Override
    public BinResponseDTO updateBin(Long id, BinRequestDTO dto) {
        Bin bin = binRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bin not found with id: " + id));

        if (!bin.getBinCode().equals(dto.getBinCode())
                && binRepository.existsByBinCodeAndRack_RackId(dto.getBinCode(), dto.getRackId())) {
            throw new IllegalArgumentException("Bin code already exists in this rack: " + dto.getBinCode());
        }

        Rack rack = rackRepository.findById(dto.getRackId())
                .orElseThrow(() -> new ResourceNotFoundException("Rack not found with id: " + dto.getRackId()));

        // Defensive: required fields for updating dimensions
        if (dto.getLengthCm() == null || dto.getWidthCm() == null || dto.getHeightCm() == null) {
            throw new IllegalArgumentException("Bin dimensions (length, width, height) must not be null.");
        }
        if (dto.getBinSize() == null) {
            throw new IllegalArgumentException("Bin size must not be null.");
        }

        // Only copy properties that are safe to update
        BeanUtils.copyProperties(dto, bin, "id", "rack", "usedVolume", "availableVolume", "currentUnitQuantity", "status", "occupied", "volumeCapacity");
        bin.setRack(rack);
        bin.setProductId(dto.getProductId());

        // Always recalculate volumes on update if dimensions changed
        double calculatedVolume = dto.getLengthCm() * dto.getWidthCm() * dto.getHeightCm();
        bin.setVolumeCapacity(calculatedVolume);
        // Optionally, adjust availableVolume if needed (if usedVolume > 0, available = volumeCapacity - usedVolume)
        if (bin.getUsedVolume() == null) bin.setUsedVolume(0.0);
        bin.setAvailableVolume(calculatedVolume - bin.getUsedVolume());
        if (bin.getAvailableVolume() < 0) bin.setAvailableVolume(0.0);
        if (bin.getCurrentUnitQuantity() == null) bin.setCurrentUnitQuantity(0.0);
        if (bin.getMaxUnitCapacity() == null) bin.setMaxUnitCapacity(0.0);
        if (bin.getStatus() == null) bin.setStatus(BinStatus.EMPTY);
        if (bin.getOccupied() == null) bin.setOccupied(false);

        Bin updated = binRepository.save(bin);
        return convertToDto(updated);
    }

    @Override
    public BinResponseDTO getBinById(Long id) {
        Bin bin = binRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bin not found with id: " + id));
        return convertToDto(bin);
    }

    @Override
    public List<BinResponseDTO> getBinsByRackId(Long rackId) {
        List<Bin> bins = binRepository.findByRack_RackId(rackId);
        return bins.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteBin(Long id) {
        if (!binRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bin not found with id: " + id);
        }
        binRepository.deleteById(id);
    }

    private BinResponseDTO convertToDto(Bin bin) {
        BinResponseDTO dto = new BinResponseDTO();
        BeanUtils.copyProperties(bin, dto);

        // Manual mapping for nested fields
        if (bin.getRack() != null) {
            dto.setRackId(bin.getRack().getRackId());
            if (bin.getRack().getAisle() != null && bin.getRack().getAisle().getWarehouse() != null) {
                dto.setWarehouseCode(bin.getRack().getAisle().getWarehouse().getWarehouseCode());
            }
        }

        dto.setProductId(bin.getProductId());

        // Fetch product details from product-service if productId is present
        if (bin.getProductId() != null) {
            try {
                ProductDTO product = webClient.get()
                        .uri(PRODUCT_SERVICE_BASE_URL + bin.getProductId())
                        .retrieve()
                        .bodyToMono(ProductDTO.class)
                        .block();
                dto.setProduct(product);
            } catch (Exception e) {
                log.warn("Could not fetch product details for productId {}: {}", bin.getProductId(), e.getMessage());
                dto.setProduct(null);
            }
        }

        return dto;
    }

    /**
     * Efficient bin allocation:
     * 1. Prefer PARTIAL bins with matching SKU and available capacity.
     * 2. If not enough, use EMPTY bins with available capacity.
     * 3. Never mix different SKUs in same bin.
     */
    @Override
    public List<BinLocationResponseDTO> allocateBins(BinAllocationRequestDTO request) {
        double volumePerUnit = request.getLengthCm() * request.getWidthCm() * request.getHeightCm();
        double unitsToStore = request.getUnitsToStore();

        if (unitsToStore <= 0) {
            throw new IllegalArgumentException("Units to store must be greater than 0.");
        }

        List<Bin> bins = binRepository.findAvailableBins(request.getWarehouseId());

        if (bins == null || bins.isEmpty()) {
            throw new RuntimeException("No bins found for warehouse ID: " + request.getWarehouseId());
        }

        List<Bin> partialBinsWithSku = bins.stream()
                .filter(bin -> bin.getCurrentUnitQuantity() > 0)
                .filter(bin -> request.getSkuCode().equals(bin.getStoredSkuCode()))
                .filter(bin -> bin.getAvailableVolume() > 0)
                .filter(bin -> bin.getCurrentUnitQuantity() < bin.getMaxUnitCapacity())
                .collect(Collectors.toList());

        List<Bin> emptyBins = bins.stream()
                .filter(bin -> bin.getCurrentUnitQuantity() == 0)
                .filter(bin -> bin.getAvailableVolume() > 0)
                .collect(Collectors.toList());

        List<Bin> prioritizedBins = new ArrayList<>();
        prioritizedBins.addAll(partialBinsWithSku);
        prioritizedBins.addAll(emptyBins);

        List<BinLocationResponseDTO> allocatedBins = new ArrayList<>();

        for (Bin bin : prioritizedBins) {
            log.info("Checking bin: {}, availableVolume: {}, maxUnitCapacity: {}, currentQty: {}, volumePerUnit: {}, storedSku: {}",
                    bin.getBinCode(),
                    bin.getAvailableVolume(),
                    bin.getMaxUnitCapacity(),
                    bin.getCurrentUnitQuantity(),
                    volumePerUnit,
                    bin.getStoredSkuCode()
            );

            double availableVolumeCm3 = bin.getAvailableVolume();
            double maxByVolume = Math.floor(availableVolumeCm3 / volumePerUnit);
            double maxByUnitCount = bin.getMaxUnitCapacity() - bin.getCurrentUnitQuantity();
            double availableUnits = Math.min(maxByVolume, maxByUnitCount);

            if (availableUnits < 1) {
                log.warn("Bin {} cannot store even one unit. Skipping.", bin.getBinCode());
                continue;
            }

            double unitsToAllocate = Math.min(unitsToStore, availableUnits);
            double usedVolume = unitsToAllocate * volumePerUnit;

            bin.setCurrentUnitQuantity(bin.getCurrentUnitQuantity() + unitsToAllocate);
            bin.setUsedVolume(bin.getUsedVolume() + usedVolume);
            bin.setAvailableVolume(bin.getVolumeCapacity() - bin.getUsedVolume());
            if (bin.getAvailableVolume() <= 0) {
                bin.setStatus(BinStatus.FULL);
                bin.setAvailableVolume(0.0); // Defensive
            } else if (bin.getCurrentUnitQuantity() > 0) {
                bin.setStatus(BinStatus.PARTIAL);
            } else {
                bin.setStatus(BinStatus.EMPTY);
            }
            bin.setOccupied(bin.getCurrentUnitQuantity() > 0);
            bin.setStoredSkuCode(request.getSkuCode());
            bin.setProductId(request.getProductId());
            binRepository.save(bin);

            BinLocationResponseDTO response = new BinLocationResponseDTO();
            BeanUtils.copyProperties(bin, response);
            response.setUnitsAllocated(unitsToAllocate);

            if (bin.getRack() != null && bin.getRack().getAisle() != null) {
                response.setRackCode(bin.getRack().getRackCode());
                response.setAisleCode(bin.getRack().getAisle().getAisleCode());
                response.setWarehouseCode(bin.getRack().getAisle().getWarehouse().getWarehouseCode());
            }

            allocatedBins.add(response);
            unitsToStore -= unitsToAllocate;

            log.info("Allocated {} units to bin {}, remaining units: {}", unitsToAllocate, bin.getBinCode(), unitsToStore);

            if (unitsToStore <= 0) break;
        }

        if (unitsToStore > 0) {
            throw new RuntimeException("Not enough bin capacity to store all units. Units left: " + unitsToStore);
        }

        return allocatedBins;
    }
}