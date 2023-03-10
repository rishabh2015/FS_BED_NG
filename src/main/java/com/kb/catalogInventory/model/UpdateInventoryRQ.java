package com.kb.catalogInventory.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UpdateInventoryRQ {

	@NotBlank
	@Size(min=32,message="Invalid unique Identifier")
	private String uniqueIdentifier;
	
	@NotBlank
	@Min(value=1,message="Must be greater than Zero")
	private String quantity;
	
	// need to create tables to insert row for each product fridge
	private long userId;
	
	private String cartId;
	
}
