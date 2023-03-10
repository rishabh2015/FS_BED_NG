package com.kb.catalogInventory.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceMatrix implements Serializable {
	private String id;
	private String basePriceperunit;
	private String discountOnQuantity;
	private String discountValueperUnit;
	private String range;
	private String totalPriceAfterDiscount;
	private String basePriceperunitDiscounted;
	private String lowerLimit;
	private String upperLimit;
	private String essTimeOdDelivery;
	private String startDate;
	private String endDate;
	private String displayPrice;
	private String lowerLimitSavingAmount;
	private String upperLimitSavingAmount;
	private String discountPercent;
}
