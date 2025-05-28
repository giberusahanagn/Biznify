package com.biznify.warehouse.serviceImplimentation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biznify.warehouse.entity.Bin;
import com.biznify.warehouse.entity.InboundShipment;
import com.biznify.warehouse.entity.ProductBatch;
import com.biznify.warehouse.entity.ProductBatchBinMapping;
import com.biznify.warehouse.enums.BinStatus;
import com.biznify.warehouse.repository.BinRepository;
import com.biznify.warehouse.repository.InboundShipmentRepository;
import com.biznify.warehouse.repository.ProductBatchBinMappingRepository;
import com.biznify.warehouse.repository.ProductBatchRepository;
import com.biznify.warehouse.service.ProductStorageService;

import jakarta.transaction.Transactional;

@Service
public class ProductStorageServiceImplimentation implements ProductStorageService{

    @Autowired
    private BinRepository binRepository;
    @Autowired
    private ProductBatchRepository productBatchRepository;
    @Autowired
    private InboundShipmentRepository inboundShipmentRepository;
    @Autowired
    private ProductBatchBinMappingRepository mappingRepository;

    @Override
    @Transactional
    public void storeProductBatch(Long batchId) {
        ProductBatch batch = productBatchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("ProductBatch with ID " + batchId + " not found."));

        InboundShipment shipment = batch.getInboundShipment();
        double quantityToStore = batch.getQuantity();
        double unitVolume = batch.getProduct().getUnitVolumeInCubicMeters();

        List<Bin> bins = binRepository.findBinsWithAvailableSpace(); // Should return bins with enough volume/unit space

        for (Bin bin : bins) {
            if (quantityToStore <= 0) break;

            double unitCapacityAvailable = bin.getMaxUnitCapacity() - bin.getCurrentUnitQuantity();
            double volumeCapacityAvailable = bin.getAvailableVolume();

            double maxQtyByVolume = volumeCapacityAvailable / unitVolume;
            double storableQty = Math.min(quantityToStore, Math.min(unitCapacityAvailable, maxQtyByVolume));

            if (storableQty <= 0) continue;

            // Update bin
            bin.setCurrentUnitQuantity(bin.getCurrentUnitQuantity() + storableQty);
            bin.setUsedVolume(bin.getUsedVolume() + (storableQty * unitVolume));
            bin.setOccupied(true);
            bin.setStatus(bin.getCurrentUnitQuantity() >= bin.getMaxUnitCapacity() ? BinStatus.FULL : BinStatus.PARTIALLY_FULL);
            binRepository.save(bin);

            // Create mapping
            ProductBatchBinMapping mapping = new ProductBatchBinMapping();
            mapping.setProductBatch(batch);
            mapping.setInboundShipment(shipment);
            mapping.setBin(bin);
            mapping.setRack(bin.getRack());
            mapping.setAisle(bin.getRack().getAisle());
            mapping.setQuantityStored(storableQty);
            mapping.setStoredAt(LocalDateTime.now());

            mappingRepository.save(mapping);

            quantityToStore -= storableQty;
        }

        if (quantityToStore > 0) {
            throw new RuntimeException("Not enough bin space to store entire product batch.");
        }
    }
}
