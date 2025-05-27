package com.biznify.warehouse.serviceImplimentation;

import com.biznify.warehouse.dto.AisleDTO;
import com.biznify.warehouse.dto.RackDTO;
import com.biznify.warehouse.entity.Aisle;
import com.biznify.warehouse.repository.AisleRepository;
import com.biznify.warehouse.service.AisleService;
import com.biznify.warehouse.service.RackService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class AisleServiceImplimentation implements AisleService {

	@Autowired
	private AisleRepository aisleRepository;
	@Autowired
	private RackService rackService;

	@Override
	public List<AisleDTO> getAislesByWarehouseCode(String warehouseCode) {
		List<Aisle> aisles = aisleRepository.findByWarehouse_WarehouseCode(warehouseCode);

		return aisles.stream().map(aisle -> {
			AisleDTO dto = new AisleDTO();
			BeanUtils.copyProperties(aisle, dto);
			dto.setRacks(rackService.getRacksByAisleCode(aisle.getAisleCode()));
			return dto;
		}).collect(Collectors.toList());
	}
}