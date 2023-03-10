package com.kb.catalogInventory.model;

import java.util.List;

import lombok.Data;

@Data
public class ProductVariationBO {

	private List<VariationBO> productVariationOptionValue;
	
	private String productCombinationValue;
}
