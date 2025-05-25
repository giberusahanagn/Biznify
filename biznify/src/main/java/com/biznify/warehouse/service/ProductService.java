package com.biznify.warehouse.service;


import org.springframework.stereotype.Service;

import com.biznify.warehouse.entity.Product;

@Service
public interface ProductService {

	// ProductDTO addProduct(ProductDTO productDTO);

	Product addProduct(Product product);

	
	Product addProductToWarehouse(String warehouseCode, Product product, int quantity);
	 
	 
//	    ProductDTO updateProduct(Long id, ProductDTO productDTO);
//	    void deleteProduct(Long id);
//	    List<ProductDTO> getAllProducts();
//	    ProductDTO getProductById(Long id);
}
