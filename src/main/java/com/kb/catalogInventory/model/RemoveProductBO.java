package com.kb.catalogInventory.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemoveProductBO {

	private String productCombinationId;
	 
	private Integer availableStock;
	
	private boolean removeProductFromList;
}
