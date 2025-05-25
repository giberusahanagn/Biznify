package com.biznify.warehouse.serviceImplimentation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biznify.warehouse.entity.*;
import com.biznify.warehouse.exception.ResourceNotFoundException;
import com.biznify.warehouse.repository.*;
import com.biznify.warehouse.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImplimentation implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private ProductBatchRepository productBatchRepository; // Add this repo

    @Override
    public Product addProduct(Product product) {
        log.info("Adding product without warehouse: {}", product.getName());
        return productRepository.save(product);
    }

    
     //Adds product to warehouse by creating a product batch in a bin with enough capacity.
     
    @Override
    public Product addProductToWarehouse(String warehouseCode, Product product, int quantity) {
//        log.info("Adding product '{}' to warehouse: {} with quantity: {}", product.getName(), warehouseCode, quantity);
//
//        Warehouse warehouse = warehouseRepository.findByWarehouseCode(warehouseCode)
//                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with code: " + warehouseCode));
//
//        // Find bins with enough available space (consider product volume * quantity)
//        double requiredVolume = product.getVolumeInCm() * quantity;
//        List<Bin> bins = binRepository.findByAvailableSpaceGreaterThanEqualAndRack_Warehouse_WarehouseCode(requiredVolume, warehouseCode);
//
//        if (bins.isEmpty()) {
//            throw new RuntimeException("No bins with enough space in warehouse: " + warehouseCode);
//        }
//
//        Bin selectedBin = bins.get(0);
//        double currentQty = selectedBin.getCurrentUnitQuantity() != null ? selectedBin.getCurrentUnitQuantity() : 0;
//        double newCurrentQty = currentQty + requiredVolume;
//
//        if (newCurrentQty > selectedBin.getMaxUnitCapacity()) {
//            throw new RuntimeException("Selected bin capacity exceeded.");
//        }
//
//        selectedBin.setCurrentUnitQuantity(newCurrentQty);
//        selectedBin.setAvailableSpace(selectedBin.getMaxUnitCapacity() - newCurrentQty);
//        binRepository.save(selectedBin);
//
//        // Save product if new
//        Product savedProduct = productRepository.save(product);
//
//        // Create a new ProductBatch and associate with bin and product
//        ProductBatch batch = new ProductBatch();
//        batch.setProduct(savedProduct);
//        batch.setBin(selectedBin);
//        batch.setQuantity(quantity);  // quantity of units stored
//        batch.setReceivedDate(LocalDate.now());
//        productBatchRepository.save(batch);
//
//        log.info("Product batch created with quantity {} in bin ID {} of warehouse {}", quantity, selectedBin.getId(), warehouseCode);
//        return savedProduct;
    	return null;
    }
}
