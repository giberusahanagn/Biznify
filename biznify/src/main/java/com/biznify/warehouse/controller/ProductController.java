package com.biznify.warehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biznify.warehouse.entity.Product;
import com.biznify.warehouse.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@PostMapping
	public ResponseEntity<Product> addProduct(@RequestBody Product product) {
		log.info("adding product in controller");
		log.info("Adding new product in controller: {}", product);
		Product savedProduct = productService.addProduct(product);
		log.info("New product added: {}", savedProduct);
		return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
	}
	
}
