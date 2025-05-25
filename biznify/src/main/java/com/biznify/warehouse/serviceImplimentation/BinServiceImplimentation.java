package com.biznify.warehouse.serviceImplimentation;

import com.biznify.warehouse.dto.BinDTO;
import com.biznify.warehouse.dto.BinResponseDTO;
import com.biznify.warehouse.dto.StorageRequestDTO;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.entity.Product;
import com.biznify.warehouse.enums.BinStatus;
import com.biznify.warehouse.repository.BinRepository;
import com.biznify.warehouse.repository.ProductRepository;
import com.biznify.warehouse.service.BinService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BinServiceImplimentation implements BinService {

	@Autowired
    private  BinRepository binRepository;

	@Autowired
	private ProductRepository productRepository;
    @Override
    public List<BinDTO> getAvailableBins(String warehouseCode) {
        return binRepository.findByRack_Warehouse_WarehouseCodeAndAvailableVolumeGreaterThan(warehouseCode, 0.0)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void recalculateBinVolume(Long binId) {
        Bin bin = binRepository.findById(binId).orElseThrow();
        bin.setUsedVolume(bin.getVolumeCapacity() * (bin.getCurrentUnitQuantity() / bin.getMaxUnitCapacity()));
        bin.setAvailableVolume(bin.getVolumeCapacity() - bin.getUsedVolume());
        bin.setOccupied(bin.getCurrentUnitQuantity() > 0);
        if (bin.getCurrentUnitQuantity() == 0) bin.setStatus(BinStatus.EMPTY);
        else if (bin.getCurrentUnitQuantity().equals(bin.getMaxUnitCapacity())) bin.setStatus(BinStatus.FULL);
        else bin.setStatus(BinStatus.PARTIALLY_FILLED);
        binRepository.save(bin);
    }

    private BinDTO toDTO(Bin bin) {
        BinDTO dto = new BinDTO();
        BeanUtils.copyProperties(bin, dto);
        return dto;
    }
    
    @Override
    public List<BinResponseDTO> findSuitableBins(StorageRequestDTO requestDTO) {
        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Calculate volume for one unit (length × width × height in cm³)
        double unitVolumeCm3 = product.getLengthCm() * product.getWidthCm() * product.getHeightCm();

        // Convert cm³ to m³ (1 m³ = 1,000,000 cm³)
        double unitVolumeM3 = unitVolumeCm3 / 1_000_000.0;

        // Total volume required
        double totalVolume = unitVolumeM3 * requestDTO.getQuantity();

        // Find all bins that can fit the required volume
        List<Bin> bins = binRepository.findByAvailableSpaceGreaterThan(totalVolume);

        // Map to DTO
        return bins.stream().map(bin -> {
            BinResponseDTO dto = new BinResponseDTO();
            dto.setBinCode(bin.getBinCode());
            dto.setAvailableSpace(bin.getAvailableVolume());
            dto.setRackCode(bin.getRack().getRackCode());
            dto.setAisleCode(bin.getRack().getAisle().getAisleCode());
            dto.setWarehouseCode(bin.getRack().getAisle().getWarehouse().getWarehouseCode());
            return dto;
        }).collect(Collectors.toList());
    }
}