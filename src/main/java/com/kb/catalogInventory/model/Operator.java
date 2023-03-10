package com.kb.catalogInventory.model;

import lombok.Data;

@Data
public class Operator {
	
	private String equalTo;
	
	private String in;
	
	private String notequalTo;
	
	private String notIn;
	
	private String greaterThan;
	
	private String lesserThan;
	
	private String between;

}
