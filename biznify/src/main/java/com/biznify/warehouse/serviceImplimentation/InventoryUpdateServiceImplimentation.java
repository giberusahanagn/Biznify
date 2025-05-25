package com.biznify.warehouse.serviceImplimentation;

import com.biznify.warehouse.dto.CurrentInventoryDTO;
import com.biznify.warehouse.entity.*;
import com.biznify.warehouse.repository.*;
import com.biznify.warehouse.service.EmailService;
import com.biznify.warehouse.service.InventoryUpdateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryUpdateServiceImplimentation implements InventoryUpdateService {

    @Autowired
    private InventoryUpdateRepository inventoryUpdateRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RackRepository rackRepository;

    @Autowired
    private BinRepository binRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private EmployeeRepository employeeRepository; // ✅ New

    @Override
    public List<InventoryUpdate> getInventoryUpdatesByProductId(Long productId) {
        return inventoryUpdateRepository.findByProductId(productId);
    }

    @Override
    public InventoryUpdate saveInventoryUpdate(InventoryUpdate inventoryUpdate) {
        if (inventoryUpdate.getProduct() == null || inventoryUpdate.getProduct().getId() == null) {
            throw new IllegalArgumentException("Product ID must be provided.");
        }
        if (inventoryUpdate.getBin() == null || inventoryUpdate.getBin().getId() == null) {
            throw new IllegalArgumentException("Bin ID must be provided.");
        }
        if (inventoryUpdate.getOperationType() == null || inventoryUpdate.getQuantity() == null) {
            throw new IllegalArgumentException("Operation type and quantity must be provided.");
        }
        if (inventoryUpdate.getEmployee() == null || inventoryUpdate.getEmployee().getEmployeeid() == null) {
            throw new IllegalArgumentException("Employee ID must be provided.");
        }

        // Validate Product
        Product product = productRepository.findById(inventoryUpdate.getProduct().getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Validate Employee
        Employee employee = employeeRepository.findById(inventoryUpdate.getEmployee().getEmployeeid())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
        inventoryUpdate.setEmployee(employee);

        // Check received quantity
        Double receivedQuantity = product.getReceivedQuantity();
        if (receivedQuantity == null || receivedQuantity == 0) {
            throw new IllegalArgumentException("Product from partner not yet received.");
        }

        // Check total IN quantity so far
        Double totalInSoFar = inventoryUpdateRepository.findTotalQuantityInByProductId(product.getId());
        totalInSoFar = totalInSoFar != null ? totalInSoFar : 0.0;

        double currentOperationQty = inventoryUpdate.getQuantity();

        if ("IN".equalsIgnoreCase(inventoryUpdate.getOperationType())) {
            if (totalInSoFar + currentOperationQty > receivedQuantity) {
                throw new IllegalArgumentException("Quantity exceeds the received amount from partner.");
            }
        }

        // Save Inventory Update
        InventoryUpdate savedUpdate = inventoryUpdateRepository.save(inventoryUpdate);

        // Update Bin
        Bin bin = binRepository.findById(inventoryUpdate.getBin().getId())
                .orElseThrow(() -> new IllegalArgumentException("Bin not found"));

        double currentBinQty = bin.getCurrentQuantity() != null ? bin.getCurrentQuantity() : 0.0;
        double newBinQty;

        if ("IN".equalsIgnoreCase(inventoryUpdate.getOperationType())) {
            newBinQty = currentBinQty + currentOperationQty;
            if (newBinQty > bin.getTotalCapacity()) {
                throw new IllegalArgumentException("Bin overflow: exceeds bin capacity.");
            }
            bin.setCurrentQuantity(newBinQty);
            bin.setAvailableSpace(bin.getTotalCapacity() - newBinQty);

            Partner partner = product.getPartner();
            if (partner != null && partner.getEmail() != null) {
                try {
                    emailService.sendProductReceivedEmail(
                            partner.getEmail(),
                            partner.getName(),
                            product.getName(),
                            inventoryUpdate.getQuantity()
                            
                    );
                } catch (Exception e) {
                    System.err.println("Failed to send product received email: " + e.getMessage());
                }
            }

        } else if ("OUT".equalsIgnoreCase(inventoryUpdate.getOperationType())) {
            newBinQty = currentBinQty - currentOperationQty;
            if (newBinQty < 0) {
                throw new IllegalArgumentException("Insufficient quantity in bin for OUT operation.");
            }
            bin.setCurrentQuantity(newBinQty);
            bin.setAvailableSpace(bin.getTotalCapacity() - newBinQty);
        } else {
            throw new IllegalArgumentException("Invalid operation type: " + inventoryUpdate.getOperationType());
        }

        binRepository.save(bin);

        return savedUpdate;
    }

    @Override
    public List<CurrentInventoryDTO> getCurrentInventory() {
        return inventoryUpdateRepository.findCurrentInventory();
    }
}
