package com.kb.catalogInventory.model;

import com.kb.catalogInventory.datatable.Supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinalPriceAndSupplierDetailModel {

	private Double basePrice;  //basecurrency,converted currency, converted price
	
	private String baseCurrency;
	
	private String convertedCurrency;
	
	private Double convertedPrice;
	
	private Supplier supplier;
	
	private Float supplierBasePrice;
	
	private Float weight;
	
	private Float height;
	
	private Float width;
	
	private Float length;
	
	private Boolean isKbPickUp=false;

	private Long productBrandId;

	

	public FinalPriceAndSupplierDetailModel(Double basePrice, Supplier supplier, Float supplierBasePrice,
			Float weight,Float height,Float width,Float length,Boolean isKbPickUp) {
		super();
		this.basePrice = basePrice;
		this.supplier = supplier;
		this.supplierBasePrice = supplierBasePrice;
		this.weight = weight;
		this.height=height;
		this.width=width;
		this.length=length;
		this.isKbPickUp=isKbPickUp;
	}
	
	
	
	
}
