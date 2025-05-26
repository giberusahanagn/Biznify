package com.biznify.warehouse.serviceImplimentation;

import com.biznify.warehouse.dto.BinDTO;
import com.biznify.warehouse.dto.RackDTO;
import com.biznify.warehouse.dto.WarehouseDTO;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.entity.Rack;
import com.biznify.warehouse.entity.Warehouse;
import com.biznify.warehouse.enums.BinStatus;
import com.biznify.warehouse.repository.RackRepository;
import com.biznify.warehouse.repository.WarehouseRepository;
import com.biznify.warehouse.service.BinService;
import com.biznify.warehouse.service.RackService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RackServiceImplimentation implements RackService {

	@Autowired
	private RackRepository rackRepository;
	@Autowired
	private BinService binService;

	@Override
	public List<RackDTO> getRacksByAisleCode(String aisleCode) {
		List<Rack> racks = rackRepository.findByAisle_AisleCode(aisleCode);
		return racks.stream().map(this::toDTO).collect(Collectors.toList());
	}

	@Override
	public void recalculateRackCapacity(Long rackId) {
		Rack rack = rackRepository.findById(rackId).orElseThrow();

		double available = rack.getBins().stream()
				.mapToDouble(bin -> bin.getAvailableVolume() != null ? bin.getAvailableVolume() : 0).sum();

		rack.setAvailableCapacity(available);
		rackRepository.save(rack);
	}

	private RackDTO toDTO(Rack rack) {
		RackDTO dto = new RackDTO();
		BeanUtils.copyProperties(rack, dto);
		if (rack.getBins() != null) {
			dto.setBins(rack.getBins().stream()
					.map(bin -> binService.getAvailableBins(bin.getRack().getAisle().getWarehouse().getWarehouseCode())
							.stream().filter(b -> b.getId().equals(bin.getId())).findFirst().orElse(null))
					.filter(b -> b != null).collect(Collectors.toList()));
		}
		return dto;
	}
}
