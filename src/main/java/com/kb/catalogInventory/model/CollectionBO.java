package com.kb.catalogInventory.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;


public class CollectionBO {
	
	
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

	public Integer getPageTypeId() {
		return pageTypeId;
	}

	public void setPageTypeId(Integer pageTypeId) {
		this.pageTypeId = pageTypeId;
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

	public List<ProductInventoryBO> getProductInventoryBo() {
		return productInventoryBo;
	}

	public void setProductInventoryBo(List<ProductInventoryBO> productInventoryBo) {
		this.productInventoryBo = productInventoryBo;
	}

	public Date getAddedOn() {
		return addedOn;
	}

	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
	}

	public Date getLastUpdatedOn() {
		return LastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		LastUpdatedOn = lastUpdatedOn;
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

	private String title;
	
	private String shortDescription;
	
	private String longDescription;

	private Long countryRuleId;

	private String whiteListedCountries;
	
	private String blackListedCountries;
	
	private Integer pageTypeId;
	
	private String pageTypeName;
	
	public String getPageTypeName() {
		return pageTypeName;
	}

	public void setPageTypeName(String pageTypeName) {
		this.pageTypeName = pageTypeName;
	}

	private String heroBanner;
	
	private String roundThumbnail;
	
	private String squareThumbnail;
	
	private List<ProductInventoryBO> productInventoryBo;
	
	private Date addedOn;
	
	private Date LastUpdatedOn;
	
	private String type;
	
	private Integer status; 
	
	private Long collectionId;
	
	private Boolean isFeatured;
	
	private String productSKUs;
	
	private Boolean isBanner;
	
	private String bannerUrl;

	private JsonNode attributes;

	public JsonNode getAttributes() {
		return attributes;
	}

	public void setAttributes(JsonNode attributes) {
		this.attributes = attributes;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	private String condition;

	public Boolean getIsBanner() {
		return isBanner;
	}

	public void setIsBanner(Boolean isBanner) {
		this.isBanner = isBanner;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public String getProductSKUs() {
		return productSKUs;
	}

	public void setProductSKUs(String productSKUs) {
		this.productSKUs = productSKUs;
	}

	public Boolean getIsFeatured() {
		return isFeatured;
	}

	public void setIsFeatured(Boolean isFeatured) {
		this.isFeatured = isFeatured;
	}

	public Long getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}

}
