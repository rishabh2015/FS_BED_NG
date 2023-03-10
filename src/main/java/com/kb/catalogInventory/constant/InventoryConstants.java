package com.kb.catalogInventory.constant;

public interface InventoryConstants {
	
	 String DEFAULT_CURRENCY_FETCH_ERROR="Could not fetch default currency from utility service";
	 String DEFAULT_CURRENCY_CONVERSIONFACTOR_FETCH_ERROR="Could not fetch currency conversion factor from utility service";
	 String IMAGE_URL_FETCH_ISSUE="Could not fetch image url";
	 String QUANTITY_NOT_AVAILABLE="Requested lock quantity is more than available quantity";
	 String ITEM_LOCK_EXCEPTION="Could not lock the items";
	 String KAFKA_JSON_PROCESSING_ERROR="JSON processing error in kafka producer/sender";
	 String EXCEL_PARSING_FAILED="Failed to parse the excel";
	 String PRICING_CALCULATOR_ERROR="Failed to calculate the pricicng";
	 String CATEGORY_CACHE_KEY = "categoryCacheKey";
	 String COLLECTION_CACHE_KEY = "collectionCacheKey";
	

}
