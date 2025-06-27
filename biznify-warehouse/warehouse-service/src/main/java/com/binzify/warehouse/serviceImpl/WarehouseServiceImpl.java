package com.binzify.warehouse.serviceImpl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.binzify.warehouse.dto.WarehouseRequestDTO;
import com.binzify.warehouse.dto.WarehouseResponseDTO;
import com.binzify.warehouse.entity.Aisle;
import com.binzify.warehouse.entity.Bin;
import com.binzify.warehouse.entity.Rack;
import com.binzify.warehouse.entity.Warehouse;
import com.binzify.warehouse.enums.BinStatus;
import com.binzify.warehouse.exception.ResourceNotFoundException;
import com.binzify.warehouse.repository.WarehouseRepository;
import com.binzify.warehouse.service.WarehouseService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public WarehouseResponseDTO createWarehouse(WarehouseRequestDTO dto) {
        if (warehouseRepository.existsByWarehouseCode(dto.getWarehouseCode())) {
            throw new IllegalArgumentException("Warehouse code already exists: " + dto.getWarehouseCode());
        }

        Warehouse warehouse = new Warehouse();
        copyDtoToEntity(dto, warehouse);

        // Set bi-directional references only for immediate parents
        if (warehouse.getAisles() != null) {
            for (Aisle aisle : warehouse.getAisles()) {
                aisle.setWarehouse(warehouse);
                if (aisle.getRacks() != null) {
                    for (Rack rack : aisle.getRacks()) {
                        rack.setAisle(aisle);
                        if (rack.getBins() != null) {
                            for (Bin bin : rack.getBins()) {
                                bin.setRack(rack);
                                setBinNotNullFields(bin);
                            }
                        }
                    }
                }
            }
        }

        Warehouse saved = warehouseRepository.save(warehouse);
        return convertToDto(saved);
    }

    @Override
    public WarehouseResponseDTO updateWarehouse(Long id, WarehouseRequestDTO dto) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
        if (!warehouse.getWarehouseCode().equals(dto.getWarehouseCode()) &&
            warehouseRepository.existsByWarehouseCode(dto.getWarehouseCode())) {
            throw new IllegalArgumentException("Warehouse code already exists: " + dto.getWarehouseCode());
        }
        copyDtoToEntity(dto, warehouse);

        // Set bi-directional references only for immediate parents
        if (warehouse.getAisles() != null) {
            for (Aisle aisle : warehouse.getAisles()) {
                aisle.setWarehouse(warehouse);
                if (aisle.getRacks() != null) {
                    for (Rack rack : aisle.getRacks()) {
                        rack.setAisle(aisle);
                        if (rack.getBins() != null) {
                            for (Bin bin : rack.getBins()) {
                                bin.setRack(rack);
                                setBinNotNullFields(bin);
                            }
                        }
                    }
                }
            }
        }

        Warehouse updated = warehouseRepository.save(warehouse);
        return convertToDto(updated);
    }

    @Override
    public WarehouseResponseDTO getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
        return convertToDto(warehouse);
    }

    @Override
    public void deleteWarehouse(Long id) {
        if (!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + id);
        }
        warehouseRepository.deleteById(id);
    }

    private WarehouseResponseDTO convertToDto(Warehouse warehouse) {
        WarehouseResponseDTO dto = new WarehouseResponseDTO();
        BeanUtils.copyProperties(warehouse, dto);
        return dto;
    }

    private void copyDtoToEntity(WarehouseRequestDTO dto, Warehouse warehouse) {
        BeanUtils.copyProperties(dto, warehouse, "aisles");

        if (warehouse.getAisles() != null) {
            warehouse.getAisles().clear(); // Prevent stale data in update
        } else {
            warehouse.setAisles(new ArrayList<>());
        }

        if (dto.getAisles() != null) {
            dto.getAisles().forEach(aisleDto -> {
                Aisle aisle = new Aisle();
                BeanUtils.copyProperties(aisleDto, aisle, "racks");
                aisle.setWarehouse(warehouse);
                aisle.setAisleSize(aisleDto.getAisleSize());

                if (aisle.getRacks() != null) {
                    aisle.getRacks().clear();
                } else {
                    aisle.setRacks(new ArrayList<>());
                }

                if (aisleDto.getRacks() != null) {
                    aisleDto.getRacks().forEach(rackDto -> {
                        Rack rack = new Rack();
                        BeanUtils.copyProperties(rackDto, rack, "bins");
                        rack.setAisle(aisle);

                        rack.setRackType(rackDto.getRackType()); // Set rackType once

                        if (rack.getBins() != null) {
                            rack.getBins().clear();
                        } else {
                            rack.setBins(new ArrayList<>());
                        }

                        if (rackDto.getBins() != null) {
                            rackDto.getBins().forEach(binDto -> {
                                Bin bin = new Bin();
                                BeanUtils.copyProperties(binDto, bin);
                                bin.setRack(rack); // Set parent
                                setBinNotNullFields(bin);
                                rack.getBins().add(bin);
                            });
                        }
                        aisle.getRacks().add(rack);
                    });
                }
                warehouse.getAisles().add(aisle);
            });
        }
    }

    /**
     * Ensures all NOT NULL fields are set for Bin before persisting.
     */
    private void setBinNotNullFields(Bin bin) {
        // Defensive: dimensions must not be null
        if (bin.getLengthCm() == null || bin.getWidthCm() == null || bin.getHeightCm() == null) {
            throw new IllegalArgumentException("Bin dimensions (length, width, height) must not be null.");
        }
        double calculatedVolume = bin.getLengthCm() * bin.getWidthCm() * bin.getHeightCm();
        bin.setVolumeCapacity(calculatedVolume);
        bin.setAvailableVolume(calculatedVolume);
        bin.setUsedVolume(0.0);
        bin.setCurrentUnitQuantity(0.0);
        if (bin.getMaxUnitCapacity() == null) bin.setMaxUnitCapacity(0.0);
        if (bin.getStatus() == null) bin.setStatus(BinStatus.EMPTY);
        bin.setOccupied(false);
    }
}