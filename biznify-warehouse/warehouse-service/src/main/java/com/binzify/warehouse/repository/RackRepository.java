package com.binzify.warehouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.binzify.warehouse.entity.Rack;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {
	   
    List<Rack> findByAisle_AisleId(Long aisleId);

    boolean existsByRackCodeAndAisle_AisleId(String rackCode, Long aisleId);
}
