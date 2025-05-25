package com.biznify.warehouse.dto;

import com.biznify.warehouse.entity.InboundShipment;
import com.biznify.warehouse.entity.Partner;
import com.biznify.warehouse.entity.Product;

import lombok.Data;

@Data
public class ProductBatchAllocationRequest {

	 private Product product;
	    private Partner partner;
	    private InboundShipment shipment;
	    private double quantity;
	    private String warehouseCode;

}
