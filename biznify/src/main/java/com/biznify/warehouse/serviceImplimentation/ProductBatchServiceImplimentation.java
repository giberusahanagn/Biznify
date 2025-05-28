package com.biznify.warehouse.serviceImplimentation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biznify.warehouse.dto.ProductBatchDTO;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.entity.ProductBatch;
import com.biznify.warehouse.entity.ProductBatchBinMapping;
import com.biznify.warehouse.repository.BinRepository;
import com.biznify.warehouse.repository.EmployeeRepository;
import com.biznify.warehouse.repository.ProductBatchBinMappingRepository;
import com.biznify.warehouse.repository.ProductBatchRepository;
import com.biznify.warehouse.repository.ProductRepository;
import com.biznify.warehouse.service.BinService;
import com.biznify.warehouse.service.ProductBatchService;

@Service
public class ProductBatchServiceImplimentation implements ProductBatchService {

    @Autowired
    private ProductBatchRepository productBatchRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BinService binService;
    
    @Autowired
    private ProductBatchBinMappingRepository productBatchBinMapping;

    @Override
    public ProductBatchDTO saveProductBatch(ProductBatchDTO dto) {
        ProductBatch productBatch = new ProductBatch();
        BeanUtils.copyProperties(dto, productBatch, "productId", "binId", "handledByEmployeeId");

        // Set product
        productBatch.setProduct(productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found")));

        // Set handled by (optional)
        if (dto.getHandledByEmployeeId() != null) {
            productBatch.setHandledBy(employeeRepository.findById(dto.getHandledByEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found")));
        }

        // Save product batch first
        ProductBatch savedBatch = productBatchRepository.save(productBatch);

        // ❗ Do not set bin on ProductBatch directly — create mapping instead if needed
        if (dto.getBinId() != null) {
            Bin bin = binRepository.findById(dto.getBinId())
                    .orElseThrow(() -> new IllegalArgumentException("Bin not found"));

            ProductBatchBinMapping mapping = new ProductBatchBinMapping();
            mapping.setProductBatch(savedBatch);
            mapping.setInboundShipment(savedBatch.getInboundShipment()); // Assuming it's already set
            mapping.setBin(bin);
            mapping.setRack(bin.getRack());
            mapping.setAisle(bin.getRack().getAisle());
            mapping.setQuantityStored(savedBatch.getQuantity());
            mapping.setStoredAt(LocalDateTime.now());

            productBatchBinMapping.save(mapping);
        }

        ProductBatchDTO savedDTO = new ProductBatchDTO();
        BeanUtils.copyProperties(savedBatch, savedDTO);
        savedDTO.setProductId(savedBatch.getProduct().getId());

        if (savedBatch.getHandledBy() != null) {
            savedDTO.setHandledByEmployeeId(savedBatch.getHandledBy().getEmployeeid());
        }

        return savedDTO;
    }


    @Override
    public List<ProductBatchDTO> getAllProductBatches() {
        List<ProductBatch> batches = productBatchRepository.findAll();
        return batches.stream().map(batch -> {
            ProductBatchDTO dto = new ProductBatchDTO();
            BeanUtils.copyProperties(batch, dto);
            dto.setProductId(batch.getProduct().getId());
            if (batch.getHandledBy() != null) {
                dto.setHandledByEmployeeId(batch.getHandledBy().getEmployeeid());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ProductBatchDTO getProductBatchById(Long id) {
        ProductBatch batch = productBatchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductBatch not found"));
        ProductBatchDTO dto = new ProductBatchDTO();
        BeanUtils.copyProperties(batch, dto);
        dto.setProductId(batch.getProduct().getId());
        if (batch.getHandledBy() != null) {
            dto.setHandledByEmployeeId(batch.getHandledBy().getEmployeeid());
        }
        return dto;
    }

    
    @Override
    public void deleteProductBatch(Long id) {
        productBatchRepository.deleteById(id);
    }
    
    @Override
    public List<ProductBatchDTO> getBatchesByProductId(Long productId) {
        List<ProductBatch> batches = productBatchRepository.findByProduct_Id(productId);
        return batches.stream().map(batch -> {
            ProductBatchDTO dto = new ProductBatchDTO();
            BeanUtils.copyProperties(batch, dto);
            dto.setProductId(batch.getProduct().getId());
            if (batch.getHandledBy() != null) {
                dto.setHandledByEmployeeId(batch.getHandledBy().getEmployeeid());
            }
            return dto;
        }).collect(Collectors.toList());
    }
    
}
