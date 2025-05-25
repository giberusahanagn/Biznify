package com.biznify.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biznify.warehouse.entity.InboundShipment;

public interface InboundShipmentRepository extends JpaRepository<InboundShipment, Long> {}


