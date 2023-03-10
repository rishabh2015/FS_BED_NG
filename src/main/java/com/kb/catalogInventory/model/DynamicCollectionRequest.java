package com.kb.catalogInventory.model;

import java.util.List;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class DynamicCollectionRequest {

	@NotNull
	private String title;
	
	@NotNull
	private String shortDescription;
	
	@NotNull
	private String longDescription;

	private Long countryRuleId;

	private String whiteListedCountries;
	
	private String blackListedCountries;
	
	private String pageTypeName;
	
	private Boolean isFeatured;
	
	private Boolean isBanner;
	
	private String bannerUrl;
	
	@NotNull
	private String heroBanner;
	
	@NotNull
	private String roundThumbnail;
	
	@NotNull
	private String squareThumbnail;
	
	private String type;
	
	private Integer status;
	
	private List<DynamicCollectionCondtion> attributes;
	
	private String condition;
	
	private Long collectionId;

}
