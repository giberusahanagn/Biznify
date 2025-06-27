package com.biznify.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.biznify.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query("SELECT p  FROM Product p WHERE p.partnerId = :partnerId")
	List<Product> findByPartnerId(@Param("partnerId") Long partnerId);

	Optional<Product> findBySku(String sku);
}
