package com.biznify.warehouse.serviceImplimentation;

import com.biznify.warehouse.dto.BinAllocationResponseDTO;
import com.biznify.warehouse.dto.BinDTO;
import com.biznify.warehouse.dto.BinResponseDTO;
import com.biznify.warehouse.dto.StorageRequestDTO;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.entity.Product;
import com.biznify.warehouse.entity.ProductBatch;
import com.biznify.warehouse.enums.BinStatus;
import com.biznify.warehouse.repository.BinRepository;
import com.biznify.warehouse.repository.ProductBatchRepository;
import com.biznify.warehouse.repository.ProductRepository;
import com.biznify.warehouse.service.BinService;

import jakarta.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BinServiceImplimentation implements BinService {

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductBatchRepository productBatchRepository;

    @Override
    public List<BinDTO> getAvailableBins(String warehouseCode) {
        List<Bin> bins = binRepository.findByRack_Aisle_Warehouse_WarehouseCode(warehouseCode);
        System.out.println("Bins found: " + bins.size());

        List<Bin> availableBins = bins.stream()
                .filter(bin -> bin.getCurrentUnitQuantity() < bin.getMaxUnitCapacity())
                .collect(Collectors.toList());

        System.out.println("Available bins count: " + availableBins.size());

        return availableBins.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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

        double unitVolumeCm3 = product.getLengthCm() * product.getWidthCm() * product.getHeightCm();
        double unitVolumeM3 = unitVolumeCm3 / 1_000_000.0;

        int quantity = requestDTO.getQuantity();
        double totalVolumeRequired = unitVolumeM3 * quantity;

        System.out.println("Warehouse Code: " + requestDTO.getWarehouseCode());
        System.out.println("Unit Volume: " + unitVolumeM3);
        System.out.println("Required Quantity: " + quantity);

        double minVolume = 0.0; // or some calculated threshold based on product size
        List<Bin> bins = binRepository.findBinsWithAvailableVolume(requestDTO.getWarehouseCode(), minVolume);

        List<BinResponseDTO> selectedBins = new ArrayList<>();
        double accumulatedVolume = 0.0;

        for (Bin bin : bins) {
            if (accumulatedVolume >= totalVolumeRequired) break;

            double availableVolume = bin.getAvailableVolume();

            BinResponseDTO dto = new BinResponseDTO();
            dto.setBinCode(bin.getBinCode());
            dto.setRackCode(bin.getRack().getRackCode());
            dto.setAisleCode(bin.getRack().getAisle().getAisleCode());
            dto.setWarehouseCode(bin.getWarehouseCode());
            dto.setAvailableSpace(availableVolume);

            int allocatableUnits = (int) Math.floor(availableVolume / unitVolumeM3);
            dto.setAllocatedUnits(allocatableUnits);

            selectedBins.add(dto);

            accumulatedVolume += availableVolume;
        }

        System.out.println("Accumulated volume: " + accumulatedVolume);
        System.out.println("Bins returned: " + selectedBins.size());

        if (accumulatedVolume < totalVolumeRequired) {
            System.out.println("Warning: Not enough space in warehouse to store requested quantity.");
            // Optional: throw exception or return partial result based on your design
        }

        return selectedBins;
    }
    @Override
    @Transactional
    public List<BinAllocationResponseDTO> allocateProductToBins(String warehouseCode, Long productId, int totalUnitsToAllocate) {
        List<Bin> bins = binRepository.findByRack_Aisle_Warehouse_WarehouseCode(warehouseCode);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<BinAllocationResponseDTO> responseList = new ArrayList<>();
        int unitsLeft = totalUnitsToAllocate;

        for (Bin bin : bins) {
            // Fetch existing units of any products in this bin
            double usedUnits = productBatchRepository.getTotalUnitsInBin(bin);
            double remainingCapacity = bin.getMaxUnitCapacity() - usedUnits;

            if (remainingCapacity <= 0) continue;

            double allocatable = Math.min(remainingCapacity, unitsLeft);

          ProductBatch  batch = productBatchRepository.findByBinAndProduct(bin, product);
                   // .orElse(new ProductBatch(bin, product, 0));

            batch.setQuantity(batch.getQuantity() + allocatable);
            productBatchRepository.save(batch);

            // Prepare response DTO
            BinAllocationResponseDTO dto = new BinAllocationResponseDTO();
            dto.setBinCode(bin.getBinCode());
            dto.setRackCode(bin.getRack().getRackCode());
            dto.setAisleCode(bin.getRack().getAisle().getAisleCode());
            dto.setWarehouseCode(warehouseCode);
            dto.setAvailableSpace((double) (bin.getMaxUnitCapacity() - (usedUnits + allocatable)));
            dto.setAllocatedUnits(allocatable);
            responseList.add(dto);

            unitsLeft -= allocatable;
            if (unitsLeft <= 0) break;
        }

        if (unitsLeft > 0) {
            throw new RuntimeException("Insufficient space. Units left unallocated: " + unitsLeft);
        }

        return responseList;
    }

	

}
