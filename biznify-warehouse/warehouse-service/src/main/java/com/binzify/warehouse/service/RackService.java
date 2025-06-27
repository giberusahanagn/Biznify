package com.binzify.warehouse.service;

import java.util.List;

import com.binzify.warehouse.dto.RackRequestDTO;
import com.binzify.warehouse.dto.RackResponseDTO;

public interface RackService {

    RackResponseDTO createRack(RackRequestDTO dto);

    RackResponseDTO updateRack(Long rackId, RackRequestDTO dto);

    RackResponseDTO getRackById(Long rackId);

    List<RackResponseDTO> getRacksByAisleId(Long aisleId);

    void deleteRack(Long rackId);
}

