package com.biznify.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.biznify.warehouse.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
