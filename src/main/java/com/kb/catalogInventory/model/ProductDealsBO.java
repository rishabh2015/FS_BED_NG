package com.kb.catalogInventory.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class ProductDealsBO {

	private int id;
	
	private String name;
	
	private Float priceVariationPercent;
	
	private Long dealsId;
	@JsonFormat(pattern ="dd-MM-yyyy")
	private Date startDate;
	@JsonFormat(pattern ="dd-MM-yyyy")
	private Date endDate;
	
}
