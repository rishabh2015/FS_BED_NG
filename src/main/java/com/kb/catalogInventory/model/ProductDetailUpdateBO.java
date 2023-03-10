package com.kb.catalogInventory.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ProductDetailUpdateBO {
	
	@NotBlank
	private String sku;
	
	@NotBlank
	private Long supplierId;
	
	private String itemName;
	
	private String productDescription;
	
	private Integer setPeices;
	
	private String supplierPrice;
	
	private String images;
	
	private Float taxPercentage;
	
	private String hsn;
	
	private Integer stock;
	
	private Boolean activate;
	
	private Integer rowId;
	private Double mrp;
	private Integer moq;
	private Float supplierDiscount;
	private Float weight;

}
