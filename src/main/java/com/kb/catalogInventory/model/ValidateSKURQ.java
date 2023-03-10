package com.kb.catalogInventory.model;

import java.util.List;

import lombok.Data;

@Data
public class ValidateSKURQ {
	
	String supplierName;
	
	String categoryName;
	
	List<String> productSKUs;

}
