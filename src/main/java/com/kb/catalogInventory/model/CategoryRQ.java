package com.kb.catalogInventory.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryRQ {

	private String categoryName;		
	private String categoryIcon;
	private boolean isNavigation;	
	private Integer categoryStage;
	private String mainCategoryIcon;
	private Long parentCategoryId;

}
