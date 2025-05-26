package com.biznify.warehouse.serviceImplimentation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.biznify.warehouse.dto.BinAllocationResponseDTO;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.repository.BinRepository;
import com.biznify.warehouse.service.AllocationService;

public class AllocationServiceImplimetation implements AllocationService{
	
	@Autowired
	private BinRepository binRepository;

	@Override
	public List<BinAllocationResponseDTO> allocateProductToBins(String warehouseCode, Long productId, int totalUnits) {
		// 1. Fetch bins with available capacity in warehouse
		List<Bin> bins = binRepository.findBinsWithAvailableSpaceInWarehouse(warehouseCode);

		List<BinAllocationResponseDTO> allocationPlan = new ArrayList<>();
		int remainingUnits = totalUnits;

		for (Bin bin : bins) {
			double availableUnits = bin.getMaxUnitCapacity()
					- (bin.getCurrentUnitQuantity() != null ? bin.getCurrentUnitQuantity() : 0);
			if (availableUnits <= 0)
				continue;

			double allocated = Math.min(availableUnits, remainingUnits);

			allocationPlan.add(new BinAllocationResponseDTO(bin.getBinCode(), allocated, bin.getWarehouseCode(),
					bin.getRack().getRackCode(), bin.getRack().getAisle().getAisleCode()));

			remainingUnits -= allocated;
			if (remainingUnits <= 0)
				break;
		}

		if (remainingUnits > 0) {
			// Not enough space, handle accordingly (throw exception or return partial
			// allocation)
		}

		return allocationPlan;
	}
}
