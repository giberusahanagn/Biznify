package com.biznify.warehouse.serviceImplimentation;

import com.biznify.warehouse.dto.ProductBatchDTO;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.entity.Employee;
import com.biznify.warehouse.entity.InboundShipment;
import com.biznify.warehouse.entity.Partner;
import com.biznify.warehouse.entity.Product;
import com.biznify.warehouse.entity.ProductBatch;
import com.biznify.warehouse.repository.BinRepository;
import com.biznify.warehouse.repository.EmployeeRepository;
import com.biznify.warehouse.repository.ProductBatchRepository;
import com.biznify.warehouse.repository.ProductRepository;
import com.biznify.warehouse.service.BinService;
import com.biznify.warehouse.service.ProductBatchService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ProductBatchDTO saveProductBatch(ProductBatchDTO dto) {
        ProductBatch productBatch = new ProductBatch();
        BeanUtils.copyProperties(dto, productBatch);

        productBatch.setProduct(productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found")));

        productBatch.setBin(binRepository.findById(dto.getBinId())
                .orElseThrow(() -> new IllegalArgumentException("Bin not found")));

        if (dto.getHandledByEmployeeId() != null) {
            productBatch.setHandledBy(employeeRepository.findById(dto.getHandledByEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found")));
        }

        ProductBatch savedBatch = productBatchRepository.save(productBatch);

        ProductBatchDTO savedDTO = new ProductBatchDTO();
        BeanUtils.copyProperties(savedBatch, savedDTO);
        return savedDTO;
    }

    @Override
    public List<ProductBatchDTO> getAllProductBatches() {
        List<ProductBatch> batches = productBatchRepository.findAll();
        return batches.stream().map(batch -> {
            ProductBatchDTO dto = new ProductBatchDTO();
            BeanUtils.copyProperties(batch, dto);
            dto.setProductId(batch.getProduct().getId());
            dto.setBinId(batch.getBin().getId());
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
        dto.setBinId(batch.getBin().getId());
        if (batch.getHandledBy() != null) {
            dto.setHandledByEmployeeId(batch.getHandledBy().getEmployeeid());
        }
        return dto;
    }

    
    @Override
    public void deleteProductBatch(Long id) {
        productBatchRepository.deleteById(id);
    }
    
    
}
