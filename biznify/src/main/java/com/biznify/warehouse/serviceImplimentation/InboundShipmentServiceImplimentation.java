package com.biznify.warehouse.serviceImplimentation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biznify.warehouse.dto.BinAllocationResponseDTO;
import com.biznify.warehouse.dto.InboundShipmentDTO;
import com.biznify.warehouse.dto.ProductBatchDTO;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.entity.InboundShipment;
import com.biznify.warehouse.entity.ProductBatch;
import com.biznify.warehouse.entity.ProductBatchBinMapping;
import com.biznify.warehouse.enums.BinStatus;
import com.biznify.warehouse.exception.ResourceNotFoundException;
import com.biznify.warehouse.repository.BinRepository;
import com.biznify.warehouse.repository.DeliveryPartnerRepository;
import com.biznify.warehouse.repository.EmployeeRepository;
import com.biznify.warehouse.repository.InboundShipmentRepository;
import com.biznify.warehouse.repository.PartnerRepository;
import com.biznify.warehouse.repository.ProductBatchBinMappingRepository;
import com.biznify.warehouse.repository.ProductBatchRepository;
import com.biznify.warehouse.repository.ProductRepository;
import com.biznify.warehouse.repository.WarehouseRepository;
import com.biznify.warehouse.service.EmailService;
import com.biznify.warehouse.service.InboundShipmentService;

import jakarta.transaction.Transactional;

@Service
public class InboundShipmentServiceImplimentation implements InboundShipmentService {

    @Autowired
    private InboundShipmentRepository inboundShipmentRepository;

    @Autowired
    private ProductBatchRepository productBatchRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BinAllocationService binAllocationService;
    
    @Autowired
    private ProductBatchBinMappingRepository productBatchBinMappingRepository;

    @Override
    @Transactional
    public InboundShipmentDTO createInboundShipment(InboundShipmentDTO dto) {
        // 1. Map DTO to entity
        InboundShipment shipment = new InboundShipment();
        BeanUtils.copyProperties(dto, shipment);

        // 2. Save shipment to get ID
        shipment = inboundShipmentRepository.save(shipment);

        List<ProductBatch> savedBatches = new ArrayList<>();
        List<ProductBatchBinMapping> binMappings = new ArrayList<>();

        for (ProductBatchDTO batchDTO : dto.getProductBatches()) {
            // 3. Allocate bins
            List<BinAllocationResponseDTO> allocations = binAllocationService.allocateProductToBins(
                    dto.getWarehouseCode(),
                    batchDTO.getProductId(),
                    (int) batchDTO.getQuantity()
            );

            ProductBatch batch = new ProductBatch();
            batch.setInboundShipment(shipment);
            batch.setProduct(productRepository.findById(batchDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found")));
            batch.setCreatedAt(LocalDateTime.now());
            batch.setUpdatedAt(LocalDateTime.now());

            ProductBatch savedBatch = productBatchRepository.save(batch);
            savedBatches.add(savedBatch);

            double remainingUnits = batchDTO.getQuantity();

            for (BinAllocationResponseDTO allocation : allocations) {
                Bin bin = binRepository.findByBinCode(allocation.getBinCode())
                        .orElseThrow(() -> new ResourceNotFoundException("Bin not found"));

                // 4. Create bin mapping
                ProductBatchBinMapping mapping = new ProductBatchBinMapping();
                mapping.setProductBatch(savedBatch);
                mapping.setInboundShipment(shipment);
                mapping.setBin(bin);
                mapping.setRack(bin.getRack());
                mapping.setAisle(bin.getRack().getAisle());
                mapping.setQuantityStored(allocation.getAllocatedUnits());
                mapping.setStoredAt(LocalDateTime.now());

                binMappings.add(mapping);

                // 5. Update bin
                double currentQty = bin.getCurrentUnitQuantity() != null ? bin.getCurrentUnitQuantity() : 0;
                double newQty = currentQty + (int) allocation.getAllocatedUnits();
                bin.setCurrentUnitQuantity(newQty);
                bin.setStatus(newQty >= bin.getMaxUnitCapacity() ? BinStatus.FULL : BinStatus.EMPTY);
                binRepository.save(bin);

                remainingUnits -= allocation.getAllocatedUnits();
                if (remainingUnits <= 0) break;
            }

            if (remainingUnits > 0) {
                throw new RuntimeException("Not enough space to store all units for product ID: " + batchDTO.getProductId());
            }
        }

        // Save bin mappings
        productBatchBinMappingRepository.saveAll(binMappings);

        // 6. Send email
        String emailBody = buildEmailBodyWithMapping(binMappings);
        emailService.sendEmail(dto.getPartnerEmail(), "Inbound Shipment Confirmation", emailBody);

        // 7. Return DTO
        InboundShipmentDTO responseDto = new InboundShipmentDTO();
        BeanUtils.copyProperties(shipment, responseDto);
        responseDto.setProductBatches(savedBatches.stream().map(this::convertToDTO).collect(Collectors.toList()));

        return responseDto;
    }


    @Override
    public List<InboundShipmentDTO> getAllInboundShipments() {
        List<InboundShipment> shipments = inboundShipmentRepository.findAll();
        return shipments.stream().map(shipment -> {
            InboundShipmentDTO dto = new InboundShipmentDTO();
            BeanUtils.copyProperties(shipment, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    private ProductBatchDTO convertToDTO(ProductBatch batch) {
        ProductBatchDTO dto = new ProductBatchDTO();
        dto.setBatchId(batch.getId());
        dto.setProductId(batch.getProduct().getId());
        dto.setCreatedAt(batch.getCreatedAt());
        dto.setUpdatedAt(batch.getUpdatedAt());
        return dto;
    }

    private String buildEmailBodyWithMapping(List<ProductBatchBinMapping> mappings) {
        StringBuilder sb = new StringBuilder("Your inbound shipment has been successfully stored:\n\n");
        for (ProductBatchBinMapping mapping : mappings) {
            sb.append("- Product ID: ").append(mapping.getProductBatch().getProduct().getId())
              .append(", Quantity Stored: ").append(mapping.getQuantityStored())
              .append(", Bin: ").append(mapping.getBin().getBinCode())
              .append(", Rack: ").append(mapping.getRack().getRackCode())
              .append(", Aisle: ").append(mapping.getAisle().getAisleCode())
              .append(", Warehouse: ").append(mapping.getBin().getWarehouseCode())
              .append("\n");
        }
        return sb.toString();
    }

}
