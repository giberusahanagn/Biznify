package com.binzify.warehouse.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.binzify.warehouse.dto.RackRequestDTO;
import com.binzify.warehouse.dto.RackResponseDTO;
import com.binzify.warehouse.entity.Aisle;
import com.binzify.warehouse.entity.Rack;
import com.binzify.warehouse.exception.ResourceNotFoundException;
import com.binzify.warehouse.repository.AisleRepository;
import com.binzify.warehouse.repository.RackRepository;
import com.binzify.warehouse.service.RackService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RackServiceImpl implements RackService {

    private final RackRepository rackRepository;
    private final AisleRepository aisleRepository;

    public RackServiceImpl(RackRepository rackRepository, AisleRepository aisleRepository) {
        this.rackRepository = rackRepository;
        this.aisleRepository = aisleRepository;
    }

    @Override
    public RackResponseDTO createRack(RackRequestDTO dto) {
        Aisle aisle = aisleRepository.findById(dto.getAisleId())
                .orElseThrow(() -> new ResourceNotFoundException("Aisle not found with id: " + dto.getAisleId()));

        if (rackRepository.existsByRackCodeAndAisle_AisleId(dto.getRackCode(), dto.getAisleId())) {
            throw new IllegalArgumentException("Rack code already exists in this aisle: " + dto.getRackCode());
        }

        Rack rack = new Rack();
        rack.setRackCode(dto.getRackCode());
        rack.setDescription(dto.getDescription());
        rack.setAisle(aisle);

        Rack saved = rackRepository.save(rack);
        return convertToDto(saved);
    }

    @Override
    public RackResponseDTO updateRack(Long id, RackRequestDTO dto) {
        Rack rack = rackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rack not found with id: " + id));

        if (!rack.getRackCode().equals(dto.getRackCode()) &&
                rackRepository.existsByRackCodeAndAisle_AisleId(dto.getRackCode(), dto.getAisleId())) {
            throw new IllegalArgumentException("Rack code already exists in this aisle: " + dto.getRackCode());
        }

        Aisle aisle = aisleRepository.findById(dto.getAisleId())
                .orElseThrow(() -> new ResourceNotFoundException("Aisle not found with id: " + dto.getAisleId()));

        rack.setRackCode(dto.getRackCode());
        rack.setDescription(dto.getDescription());
        rack.setAisle(aisle);

        Rack updated = rackRepository.save(rack);
        return convertToDto(updated);
    }

    @Override
    public RackResponseDTO getRackById(Long id) {
        Rack rack = rackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rack not found with id: " + id));
        return convertToDto(rack);
    }

    @Override
    public List<RackResponseDTO> getRacksByAisleId(Long aisleId) {
        List<Rack> racks = rackRepository.findByAisle_AisleId(aisleId);
        return racks.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteRack(Long id) {
        if (!rackRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rack not found with id: " + id);
        }
        rackRepository.deleteById(id);
    }

    private RackResponseDTO convertToDto(Rack rack) {
        RackResponseDTO dto = new RackResponseDTO();
        dto.setId(rack.getRackId());
        dto.setRackCode(rack.getRackCode());
        dto.setDescription(rack.getDescription());
        dto.setAisleId(rack.getAisle().getAisleId());
        return dto;
    }
}
