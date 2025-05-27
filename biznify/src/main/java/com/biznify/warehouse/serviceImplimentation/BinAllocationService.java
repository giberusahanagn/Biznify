package com.biznify.warehouse.serviceImplimentation;

import com.biznify.warehouse.dto.BinAllocationResponseDTO;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.repository.BinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BinAllocationService {

    @Autowired
    private BinRepository binRepository;

    public List<BinAllocationResponseDTO> allocateProductToBins(String warehouseCode, Long productId, int totalUnits) {
        List<Bin> bins = binRepository.findBinsWithAvailableSpaceInWarehouse(warehouseCode);
        List<BinAllocationResponseDTO> allocationPlan = new ArrayList<>();
        int remainingUnits = totalUnits;

        for (Bin bin : bins) {
            double availableUnits = bin.getMaxUnitCapacity() - bin.getCurrentUnitQuantity();
            if (availableUnits <= 0) continue;

            double allocated = Math.min(availableUnits, remainingUnits);
            allocationPlan.add(new BinAllocationResponseDTO(bin.getBinCode(), allocated, bin.getWarehouseCode(),
                    bin.getRack().getRackCode(), bin.getRack().getAisle().getAisleCode()));

            remainingUnits -= allocated;
            if (remainingUnits <= 0) break;
        }

        if (remainingUnits > 0) {
            // Handle insufficient space scenario
            throw new RuntimeException("Not enough space to allocate all units.");
        }

        return allocationPlan;
    }
}
