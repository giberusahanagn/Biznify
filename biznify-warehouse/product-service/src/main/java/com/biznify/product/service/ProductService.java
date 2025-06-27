package com.biznify.product.service;

import java.util.List;

import com.biznify.product.dto.PartnerWithProductsDTO;
import com.biznify.product.dto.ProductDTO;
import com.biznify.product.dto.ProductWithPartnerDTO;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO getProductById(Long id);
    List<ProductDTO> getAllProducts();
	ProductWithPartnerDTO getProductWithPartner(Long id);
	ProductDTO getProductBySku(String sku);
	List<ProductDTO> getByPartnerId(Long partnerId);
	PartnerWithProductsDTO getPartnerWithProducts(Long partnerId);
}
