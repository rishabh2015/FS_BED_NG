package com.kb.catalogInventory.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandModelCategoryBO extends BrandModelBO {
	private long  brandModelCategoryId;
	
    private String categoryName;
}
