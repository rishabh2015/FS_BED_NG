package com.kb.catalogInventory.util;

public interface InventoryConstants {
	
	public static final String DEFAULT_CURRENCY_FETCH_ERROR="Could not fetch default currency from utility service";
	public static final String DEFAULT_CURRENCY_CONVERSIONFACTOR_FETCH_ERROR="Could not fetch currency conversion factor from utility service";
	public static final String IMAGE_URL_FETCH_ISSUE="Could not fetch image url";
	public static final String QUANTITY_NOT_AVAILABLE="Requested lock quantity is more than available quantity";
	public static final String ITEM_LOCK_EXCEPTION="Could not lock the items";
	public static final String KAFKA_JSON_PROCESSING_ERROR="JSON processing error in kafka producer/sender";
	public static final String EXCEL_PARSING_FAILED="Failed to parse the excel";
	public static final String PRICING_CALCULATOR_ERROR="Failed to calculate the pricicng";
	

}
