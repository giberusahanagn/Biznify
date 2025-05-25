package com.biznify.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biznify.warehouse.entity.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner,Long>{

}
