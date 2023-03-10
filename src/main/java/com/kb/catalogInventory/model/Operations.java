package com.kb.catalogInventory.model;

import java.util.Map;

import lombok.Data;

@Data
public class Operations {

	/*private String equalToOperator;    
	private String inOperator;
    private String notEqualToOperator;
    private String notInOperator;	
    private String lesserThanOperator;	
    private String greaterThanOperator;	
    private String betweenOperator;*/
	
	private Map<String,String> operations;
	
}
