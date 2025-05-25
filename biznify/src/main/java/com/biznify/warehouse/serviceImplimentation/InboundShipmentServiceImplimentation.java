package com.biznify.warehouse.serviceImplimentation;

import com.biznify.warehouse.dto.InboundShipmentDTO;
import com.biznify.warehouse.dto.ProductBatchDTO;
import com.biznify.warehouse.entity.InboundShipment;
import com.biznify.warehouse.entity.ProductBatch;
import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.repository.*;
import com.biznify.warehouse.service.EmailService;
import com.biznify.warehouse.service.InboundShipmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    @Transactional
    public InboundShipmentDTO createInboundShipment(InboundShipmentDTO dto) {
        InboundShipment shipment = new InboundShipment();
        BeanUtils.copyProperties(dto, shipment);

        shipment.setPartner(partnerRepository.findById(dto.getPartnerId())
                .orElseThrow(() -> new IllegalArgumentException("Partner not found")));
        shipment.setWarehouse(warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found")));

        if (dto.getDeliveryPartnerId() != null) {
            shipment.setDeliveryPartner(deliveryPartnerRepository.findById(dto.getDeliveryPartnerId())
                    .orElseThrow(() -> new IllegalArgumentException("Delivery partner not found")));
        }

        if (dto.getReceivedByEmployeeId() != null) {
            shipment.setReceivedBy(employeeRepository.findById(dto.getReceivedByEmployeeId())
                    .orElseThrow(() -> new IllegalArgumentException("Employee not found")));
        }

        List<ProductBatch> batchList = new ArrayList<>();
        if (dto.getProductBatches() != null) {
            for (ProductBatchDTO batchDTO : dto.getProductBatches()) {
                ProductBatch batch = new ProductBatch();
                BeanUtils.copyProperties(batchDTO, batch);
                batch.setInboundShipment(shipment);
                batch.setProduct(productRepository.findById(batchDTO.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found")));
                Bin bin = binRepository.findById(batchDTO.getBinId())
                        .orElseThrow(() -> new IllegalArgumentException("Bin not found"));

                // Validate available capacity in units
                double currentQuantity = bin.getCurrentUnitQuantity() != null ? bin.getCurrentUnitQuantity() : 0;
                double maxCapacity = bin.getMaxUnitCapacity();
                int batchQuantity = (int) batchDTO.getQuantity();

                if (currentQuantity + batchQuantity > maxCapacity) {
                    throw new IllegalArgumentException("Bin " + bin.getBinCode() + " cannot hold " + batchQuantity + " more units. Available: " + (maxCapacity - currentQuantity));
                }

                // Update bin current quantity
                bin.setCurrentUnitQuantity( (currentQuantity + batchQuantity));
                binRepository.save(bin);

                batch.setBin(bin);

                if (batchDTO.getHandledByEmployeeId() != null) {
                    batch.setHandledBy(employeeRepository.findById(batchDTO.getHandledByEmployeeId())
                            .orElseThrow(() -> new IllegalArgumentException("Employee not found")));
                }

                batchList.add(batch);
            }
        }

        shipment.setProductBatches(batchList);
        InboundShipment savedShipment = inboundShipmentRepository.save(shipment);

        // Send email to partner
        String partnerEmail = savedShipment.getPartner().getEmail();
        String subject = "Inbound Shipment Received: " + savedShipment.getInvoiceNumber();
        String body = "Dear " + savedShipment.getPartner().getName() + ",\n\n"
                + "We have successfully received your shipment.\n\n"
                + "Invoice Number: " + savedShipment.getInvoiceNumber() + "\n"
                + "Reference Number: " + savedShipment.getReferenceNumber() + "\n"
                + "Received At: " + savedShipment.getArrivalDate() + "\n"
                + "Received By: " + (savedShipment.getReceivedBy() != null ? savedShipment.getReceivedBy().getName() : "N/A") + "\n"
                + "Warehouse: " + savedShipment.getWarehouse().getName() + "\n\n"
                + "Remarks: " + savedShipment.getRemarks() + "\n\n"
                + "Thank you for partnering with Biznify.\n\n"
                + "Regards,\nBiznify Warehouse Team";

        try {
            emailService.sendEmail(partnerEmail, subject, body);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }

        InboundShipmentDTO savedDTO = new InboundShipmentDTO();
        BeanUtils.copyProperties(savedShipment, savedDTO);
        return savedDTO;
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
}
