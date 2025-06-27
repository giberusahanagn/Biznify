package com.biznify.partner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biznify.partner.entity.Partner;

@Repository
public interface PartnerRepository extends JpaRepository<Partner,Long>{
	List<Partner> findByNameContainingIgnoreCase(String name);

}
