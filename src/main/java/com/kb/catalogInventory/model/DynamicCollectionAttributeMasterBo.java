package com.kb.catalogInventory.model;

import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class DynamicCollectionAttributeMasterBo {

	 	private String attribute;
	    	    
	    private String columnName;
	    
	    private Map<String,String> operations;
	
	    private Set<String> values;
}
