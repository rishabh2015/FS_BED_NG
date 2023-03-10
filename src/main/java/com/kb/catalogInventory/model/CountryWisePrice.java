package com.kb.catalogInventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryWisePrice {

	private Double displayPrice= Double.valueOf(0);
	
	private Double displayPriceInter= Double.valueOf(0);
	
	private Double priceAfterDiscount=Double.valueOf(0);
	
	private Double priceAfterDiscountInter=Double.valueOf(0);
	
	private Double appliedMargin=Double.valueOf(1);
	
	private Double appliedMarginInter=Double.valueOf(1);
	
	private String countryCode=String.valueOf("In");
	
	private Double diplayPriceBeforeTax= Double.valueOf(0);
	
	private Double diplayPriceBeforeTaxInter= Double.valueOf(0);
}
