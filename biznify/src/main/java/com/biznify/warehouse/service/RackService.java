package com.biznify.warehouse.service;

import java.util.List;

import com.biznify.warehouse.dto.RackDTO;
import com.biznify.warehouse.entity.Rack;

public interface RackService {
	 List<RackDTO> getRacksByAisleCode(String aisleCode);
	    void recalculateRackCapacity(Long rackId);
}
