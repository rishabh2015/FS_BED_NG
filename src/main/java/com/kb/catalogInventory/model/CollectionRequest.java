package com.kb.catalogInventory.model;

import com.sun.istack.NotNull;

public class CollectionRequest {
	
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
	
	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public Boolean getIsBanner() {
		return isBanner;
	}

	public void setIsBanner(Boolean isBanner) {
		this.isBanner = isBanner;
	}

	public Boolean getIsFeatured() {
		return isFeatured;
	}

	public void setIsFeatured(Boolean isFeatured) {
		this.isFeatured = isFeatured;
	}

	public String getPageTypeName() {
		return pageTypeName;
	}

	public void setPageTypeName(String pageTypeName) {
		this.pageTypeName = pageTypeName;
	}
	
	@NotNull
	private String heroBanner;
	
	@NotNull
	private String roundThumbnail;
	
	@NotNull
	private String squareThumbnail;
	
	private String productSKUs;
	
	private String type;
	
	private Integer status;
	
	private Long collectionId;

	private String attributes;

	private String condition;

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public Long getCountryRuleId() {
		return countryRuleId;
	}

	public void setCountryRuleId(Long countryRuleId) {
		this.countryRuleId = countryRuleId;
	}

	public String getWhiteListedCountries() {
		return whiteListedCountries;
	}

	public void setWhiteListedCountries(String whiteListedCountries) {
		this.whiteListedCountries = whiteListedCountries;
	}

	public String getBlackListedCountries() {
		return blackListedCountries;
	}

	public void setBlackListedCountries(String blackListedCountries) {
		this.blackListedCountries = blackListedCountries;
	}

	public String getHeroBanner() {
		return heroBanner;
	}

	public void setHeroBanner(String heroBanner) {
		this.heroBanner = heroBanner;
	}

	public String getRoundThumbnail() {
		return roundThumbnail;
	}

	public void setRoundThumbnail(String roundThumbnail) {
		this.roundThumbnail = roundThumbnail;
	}

	public String getSquareThumbnail() {
		return squareThumbnail;
	}

	public void setSquareThumbnail(String squareThumbnail) {
		this.squareThumbnail = squareThumbnail;
	}

	public String getProductSKUs() {
		return productSKUs;
	}

	public void setProductSKUs(String productSKUs) {
		this.productSKUs = productSKUs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	} 

}
