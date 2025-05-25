package com.biznify.warehouse.controller;

import com.biznify.warehouse.dto.RackDTO;
import com.biznify.warehouse.service.RackService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/racks")
@RequiredArgsConstructor
public class RackController {

	private final RackService rackService;

    @GetMapping("/by-aisle")
    public List<RackDTO> getRacksByAisle(@RequestParam String aisleCode) {
        return rackService.getRacksByAisleCode(aisleCode);
    }

    @PostMapping("/{id}/recalculate")
    public void recalculateRackCapacity(@PathVariable Long id) {
        rackService.recalculateRackCapacity(id);
    }
}
