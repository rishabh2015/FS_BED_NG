package com.kb.catalogInventory.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimilarProductBO {
	private String categoryName;
	private String  brandModel;
	private String  uniqueIdentifierForSearch;
	private String thumbnailURL;
	private String productClubingId;
	private String itemName;
	private String brandName;
	private Float ratingCount;
	private boolean inStock;
	private boolean isPremiumProduct;
}
