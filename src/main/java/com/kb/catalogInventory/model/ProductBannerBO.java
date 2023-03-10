package com.kb.catalogInventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBannerBO implements Comparable<ProductBannerBO>{

	private Long id;
	
	private String imageURL;
	
	private boolean isActive;
	
	private String searchKeyword;
	
	private String heading;
	
	private String shortTitle;
	
	private String description;
	
	private Long sortOrder;
	
	private String bannerStage;
	
	private String bannerCallBackUrl;
	
	private String bannerUrl;
	private Boolean isCategoryBanner;

	@JsonFormat(pattern = "MMM dd YYYY")
	private Date createdOn;
	@JsonFormat(pattern = "MMM dd YYYY")
	private Date updatedOn;



	@Override
	public int compareTo(ProductBannerBO o) {




		return o.sortOrder.compareTo(this.sortOrder);

	}
}
