package com.biznify.inbound.repository;

import com.biznify.inbound.entity.InboundShipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboundShipmentRepository extends JpaRepository<InboundShipment, Long> {
}
