package com.kb.catalogInventory.model;

import java.util.List;

import lombok.Data;

@Data
public class DynamicCollectionCondtion {
		
	private String name;

	private String operator;
	
	private List<String> values;
	
}
