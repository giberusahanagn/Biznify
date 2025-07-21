package com.biznify.product.controller;

import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.biznify.product.dto.PartnerWithProductsDTO;
import com.biznify.product.dto.ProductDTO;
import com.biznify.product.dto.ProductWithPartnerDTO;
import com.biznify.product.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @PostConstruct
    public void loaded() {
        System.out.println("✅ ProductController loaded successfully");
    }
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO dto) {
        System.out.println("Entered create method and calling service ");
        return new ResponseEntity<>(productService.createProduct(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAll() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductDTO> getBySku(@PathVariable("sku") String sku) {
        return ResponseEntity.ok(productService.getProductBySku(sku));
    }

    @GetMapping("/{id}/with-partner")
    public ResponseEntity<ProductWithPartnerDTO> getWithPartner(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductWithPartner(id));
    }

    @GetMapping("/partner/{partnerId}")
    public ResponseEntity<PartnerWithProductsDTO> getPartnerWithProducts(@PathVariable("partnerId") Long partnerId) {
        return ResponseEntity.ok(productService.getPartnerWithProducts(partnerId));
    }
}