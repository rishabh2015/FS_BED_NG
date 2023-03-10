package com.kb.catalogInventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrice implements Serializable {

	private Double productMrp;

	private Double supplierPricePerUnit;

	private Double supplierDiscountAppliedPerUnit;

	private Double supplierDiscountPercentage;

	private Double priceAfterSupplierDiscountPerUnit;

	private Double supplierTaxPercent;

	private Double supplierTaxApplied;

	private Double finalPricePerUnit;

	private Boolean isSetPrice;

	private Double supplierSetPrice;

	private Double applicableDiscountPercent;

	private Boolean isSlashedPrice;

	private Integer setPieces;
	
	private Double productMargin=Double.valueOf(1);
	
	private Double priceAfterApplyingMarginPerUnit=Double.valueOf(1);
	
	private Double setPriceAfterApplyingMargin=Double.valueOf(1);
	
	private String marginCountry="IN";
	
    private Double moqPrice;
	
	private Integer moq;
	private Double marginedPriceWithoutDiscountWithTax;
	private  Double appliedDiscountPercentageOnMarginPrice;
	private Double retailMargin;
}
