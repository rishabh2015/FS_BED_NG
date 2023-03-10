package com.kb.catalogInventory.model;

import lombok.Data;

@Data
public class ProductStockBO {

	private Integer availableStock;

	private String uuid;
	
	private Boolean isArchived=Boolean.valueOf(false);


}
