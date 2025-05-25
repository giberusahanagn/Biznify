package com.biznify.warehouse.serviceImplimentation;

import com.biznify.warehouse.dto.AisleDTO;
import com.biznify.warehouse.dto.BinDTO;
import com.biznify.warehouse.dto.RackDTO;
import com.biznify.warehouse.dto.WarehouseDTO;
import com.biznify.warehouse.entity.Aisle;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.entity.Rack;
import com.biznify.warehouse.entity.Warehouse;
import com.biznify.warehouse.repository.AisleRepository;
import com.biznify.warehouse.repository.BinRepository;
import com.biznify.warehouse.repository.RackRepository;
import com.biznify.warehouse.repository.WarehouseRepository;
import com.biznify.warehouse.service.WarehouseService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImplimentaion implements WarehouseService {

	@Autowired
	private WarehouseRepository warehouseRepository;
	@Autowired
	private AisleRepository aisleRepository;
	@Autowired
	private RackRepository rackRepository;
	@Autowired
	private BinRepository binRepository;

	@Override
	public WarehouseDTO getFullWarehouseStructure(String warehouseCode) {
		Warehouse warehouse = warehouseRepository.findByWarehouseCode(warehouseCode)
				.orElseThrow(() -> new RuntimeException("Warehouse not found with code: " + warehouseCode));

		WarehouseDTO warehouseDTO = new WarehouseDTO();
		BeanUtils.copyProperties(warehouse, warehouseDTO);

		if (warehouse.getAisles() != null) {
			warehouseDTO.setAisles(
					warehouse.getAisles().stream().map(this::convertAisleToDTO).collect(Collectors.toList()));
		}

		return warehouseDTO;
	}

	private AisleDTO convertAisleToDTO(Aisle aisle) {
		AisleDTO dto = new AisleDTO();
		BeanUtils.copyProperties(aisle, dto);

		if (aisle.getRacks() != null) {
			dto.setRacks(aisle.getRacks().stream().map(this::convertRackToDTO).collect(Collectors.toList()));
		}

		return dto;
	}

	private RackDTO convertRackToDTO(Rack rack) {
		RackDTO dto = new RackDTO();
		BeanUtils.copyProperties(rack, dto);

		if (rack.getBins() != null) {
			dto.setBins(rack.getBins().stream().map(this::convertBinToDTO).collect(Collectors.toList()));
		}

		return dto;
	}

	private BinDTO convertBinToDTO(Bin bin) {
		BinDTO dto = new BinDTO();
		BeanUtils.copyProperties(bin, dto);
		return dto;
	}

	@Override
	@Transactional
	public WarehouseDTO createWarehouseWithStructure(WarehouseDTO warehouseDTO) {
		Warehouse warehouse = new Warehouse();
		BeanUtils.copyProperties(warehouseDTO, warehouse);
		warehouse = warehouseRepository.save(warehouse);

		if (warehouseDTO.getAisles() != null) {
			for (AisleDTO aisleDTO : warehouseDTO.getAisles()) {
				Aisle aisle = new Aisle();
				BeanUtils.copyProperties(aisleDTO, aisle);
				aisle.setWarehouse(warehouse);
				aisle = aisleRepository.save(aisle);

				if (aisleDTO.getRacks() != null) {
					for (RackDTO rackDTO : aisleDTO.getRacks()) {
						Rack rack = new Rack();
						BeanUtils.copyProperties(rackDTO, rack);
						
						rack.setAisle(aisle); // assuming you added aisle field in Rack entity
						rack = rackRepository.save(rack);

						if (rackDTO.getBins() != null) {
							for (BinDTO binDTO : rackDTO.getBins()) {
								Bin bin = new Bin();
								BeanUtils.copyProperties(binDTO, bin);
								bin.setRack(rack);
								bin = binRepository.save(bin);
							}
						}
					}
				}
			}
		}

		return convertToDTO(warehouse);
	}

	private WarehouseDTO convertToDTO(Warehouse warehouse) {
		WarehouseDTO dto = new WarehouseDTO();
		BeanUtils.copyProperties(warehouse, dto);

		List<AisleDTO> aisleDTOs = warehouse.getAisles().stream().map(aisle -> {
			AisleDTO aisleDTO = new AisleDTO();
			BeanUtils.copyProperties(aisle, aisleDTO);

			List<RackDTO> rackDTOs = aisle.getRacks().stream().map(rack -> {
				RackDTO rackDTO = new RackDTO();
				BeanUtils.copyProperties(rack, rackDTO);

				List<BinDTO> binDTOs = rack.getBins().stream().map(bin -> {
					BinDTO binDTO = new BinDTO();
					BeanUtils.copyProperties(bin, binDTO);
					return binDTO;
				}).collect(Collectors.toList());

				rackDTO.setBins(binDTOs);
				return rackDTO;
			}).collect(Collectors.toList());

			aisleDTO.setRacks(rackDTOs);
			return aisleDTO;
		}).collect(Collectors.toList());

		dto.setAisles(aisleDTOs);

		return dto;
	}
}