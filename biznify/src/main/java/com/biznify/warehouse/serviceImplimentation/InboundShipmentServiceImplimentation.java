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
import com.biznify.warehouse.enums.BinStatus;
import com.biznify.warehouse.exception.InsufficientSpaceException;
import com.biznify.warehouse.exception.ResourceNotFoundException;
import com.biznify.warehouse.repository.BinRepository;
import com.biznify.warehouse.repository.DeliveryPartnerRepository;
import com.biznify.warehouse.repository.EmployeeRepository;
import com.biznify.warehouse.repository.InboundShipmentRepository;
import com.biznify.warehouse.repository.PartnerRepository;
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

    @Override
    @Transactional
    public InboundShipmentDTO createInboundShipment(InboundShipmentDTO dto) throws InsufficientSpaceException {
        // 1. Map DTO to entity
        InboundShipment shipment = new InboundShipment();
        BeanUtils.copyProperties(dto, shipment);

        // 2. Save shipment to get ID
        shipment = inboundShipmentRepository.save(shipment);

        List<ProductBatch> savedBatches = new ArrayList<>();

        for (ProductBatchDTO batchDTO : dto.getProductBatches()) {
            List<BinAllocationResponseDTO> allocations = binAllocationService.allocateProductToBins(
                    dto.getWarehouseCode(), batchDTO.getProductId(), (int) batchDTO.getQuantity());

            double remainingUnits = batchDTO.getQuantity();

            for (BinAllocationResponseDTO allocation : allocations) {
                Bin bin = binRepository.findByBinCode(allocation.getBinCode())
                        .orElseThrow(() -> new ResourceNotFoundException("Bin not found"));

                ProductBatch batch = new ProductBatch();
                batch.setInboundShipment(shipment);
                batch.setProduct(productRepository.findById(batchDTO.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found")));

                // use double quantity in ProductBatch if your domain allows
                batch.setQuantity(allocation.getAllocatedUnits());

                batch.setBin(bin);
                batch.setCreatedAt(LocalDateTime.now());
                batch.setUpdatedAt(LocalDateTime.now());

                savedBatches.add(productBatchRepository.save(batch));

                double currentQty = bin.getCurrentUnitQuantity() != null ? bin.getCurrentUnitQuantity() : 0;
                double newQty = currentQty + allocation.getAllocatedUnits();
                bin.setCurrentUnitQuantity(newQty);

                if (newQty == 0) {
                    bin.setStatus(BinStatus.EMPTY);
                } else if (newQty >= bin.getMaxUnitCapacity()) {
                    bin.setStatus(BinStatus.FULL);
                } else {
                    bin.setStatus(BinStatus.PARTIALLY_FULL);
                }
                binRepository.save(bin);

                remainingUnits -= allocation.getAllocatedUnits();
                if (remainingUnits <= 0) break;
            }

            if (remainingUnits > 0) {
                throw new InsufficientSpaceException("Not enough space to store all units for product ID: " + batchDTO.getProductId());
            }

        }

        // 3. Send email
        String emailBody = buildEmailBody(savedBatches);
        emailService.sendEmail(dto.getPartnerEmail(), "Inbound Shipment Confirmation", emailBody);

        // 4. Prepare response DTO
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
        dto.setQuantity(batch.getQuantity());
        dto.setBinCode(batch.getBin().getBinCode());
        dto.setCreatedAt(batch.getCreatedAt());
        dto.setUpdatedAt(batch.getUpdatedAt());
        return dto;
    }

    private String buildEmailBody(List<ProductBatch> batches) {
        StringBuilder sb = new StringBuilder("Your inbound shipment has been successfully stored:\n\n");
        for (ProductBatch batch : batches) {
            sb.append("- Product ID: ").append(batch.getProduct().getId())
              .append(", Quantity: ").append(batch.getQuantity())
              .append(", Bin: ").append(batch.getBin().getBinCode())
              .append(", Rack: ").append(batch.getBin().getRack().getRackCode())
              .append(", Warehouse: ").append(batch.getBin().getWarehouseCode())
              .append("\n");
        }
        return sb.toString();
    }
}
