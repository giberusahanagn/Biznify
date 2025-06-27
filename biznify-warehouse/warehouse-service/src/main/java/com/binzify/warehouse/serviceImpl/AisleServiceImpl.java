package com.binzify.warehouse.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.binzify.warehouse.dto.AisleRequestDTO;
import com.binzify.warehouse.dto.AisleResponseDTO;
import com.binzify.warehouse.entity.Aisle;
import com.binzify.warehouse.entity.Warehouse;
import com.binzify.warehouse.exception.ResourceNotFoundException;
import com.binzify.warehouse.repository.AisleRepository;
import com.binzify.warehouse.repository.WarehouseRepository;
import com.binzify.warehouse.service.AisleService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AisleServiceImpl implements AisleService {

    private final AisleRepository aisleRepository;
    private final WarehouseRepository warehouseRepository;

    public AisleServiceImpl(AisleRepository aisleRepository, WarehouseRepository warehouseRepository) {
        this.aisleRepository = aisleRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public AisleResponseDTO createAisle(AisleRequestDTO dto) {
        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + dto.getWarehouseId()));

        if (aisleRepository.existsByAisleCodeAndWarehouse_WarehouseId(dto.getAisleCode(), dto.getWarehouseId())) {
            throw new IllegalArgumentException("Aisle code already exists in this warehouse: " + dto.getAisleCode());
        }

        Aisle aisle = new Aisle();
        aisle.setAisleCode(dto.getAisleCode());
        aisle.setDescription(dto.getDescription());
        aisle.setWarehouse(warehouse);

        Aisle saved = aisleRepository.save(aisle);
        return convertToDto(saved);
    }

    @Override
    public AisleResponseDTO updateAisle(Long id, AisleRequestDTO dto) {
        Aisle aisle = aisleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aisle not found with id: " + id));

        if (!aisle.getAisleCode().equals(dto.getAisleCode()) &&
            aisleRepository.existsByAisleCodeAndWarehouse_WarehouseId(dto.getAisleCode(), dto.getWarehouseId())) {
            throw new IllegalArgumentException("Aisle code already exists in this warehouse: " + dto.getAisleCode());
        }

        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + dto.getWarehouseId()));

        aisle.setAisleCode(dto.getAisleCode());
        aisle.setDescription(dto.getDescription());
        aisle.setWarehouse(warehouse);

        Aisle updated = aisleRepository.save(aisle);
        return convertToDto(updated);
    }

    @Override
    public AisleResponseDTO getAisleById(Long id) {
        Aisle aisle = aisleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aisle not found with id: " + id));
        return convertToDto(aisle);
    }

    @Override
    public List<AisleResponseDTO> getAislesByWarehouseId(Long warehouseId) {
        List<Aisle> aisles = aisleRepository.findByWarehouse_WarehouseId(warehouseId);
        return aisles.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteAisle(Long id) {
        if (!aisleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aisle not found with id: " + id);
        }
        aisleRepository.deleteById(id);
    }

    private AisleResponseDTO convertToDto(Aisle aisle) {
        AisleResponseDTO dto = new AisleResponseDTO();
        dto.setAisleId(aisle.getAisleId());
        dto.setAisleCode(aisle.getAisleCode());
        dto.setDescription(aisle.getDescription());
        dto.setWarehouseId(aisle.getWarehouse().getWarehouseId());
        return dto;
    }
}
