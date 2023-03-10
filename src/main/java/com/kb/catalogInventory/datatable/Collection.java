package com.kb.catalogInventory.datatable;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="collection")
public class Collection {
	
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

	public Collection(String title, String shortDescription, String longDescription,
			ProductCountryRule productCountryRule, String whiteListedCountries, String blackListedCountries,
			Integer pageTypeId, String heroBanner, String roundThumbnail, String squareThumbnail, String productSkus,
			Date addedOn, Date lastUpdatedOn, String type, Integer status,Boolean isFeatured,Boolean isBanner,String attributes,String condition) {
		super();
		this.title = title;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.productCountryRule = productCountryRule;
		this.whiteListedCountries = whiteListedCountries;
		this.blackListedCountries = blackListedCountries;
		this.pageTypeId = pageTypeId;
		this.heroBanner = heroBanner;
		this.roundThumbnail = roundThumbnail;
		this.squareThumbnail = squareThumbnail;
		this.productSkus = productSkus;
		this.addedOn = addedOn;
		this.updatedOn = lastUpdatedOn;
		this.type = type;
		this.status = status;
		this.isFeatured = isFeatured;
		this.isBanner=isBanner;
		this.attributes=attributes;
		this.condition=condition;
	}

	public Collection() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "short_description",length =1000)
	private String shortDescription;

	@Column(name = "long_description",length =2000)
	private String longDescription;
	
	@JoinColumn(name = "country_rule_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private ProductCountryRule productCountryRule;

	
	@Column(name="white_listed_countries")
	private String whiteListedCountries;
	
	@Column(name="black_listed_countries")
	private String blackListedCountries;
	
	@Column(name="page_type_id")
	private Integer pageTypeId;
	
	@Column(name="hero_banner",length=500)
	private String heroBanner;
	
	@Column(name="round_thumbnail",length=500)
	private String roundThumbnail;
	
	@Column(name="square_thumbnail",length=500)
	private String squareThumbnail;
	
	@Column(name = "product_skus")
	private String productSkus;
	
	@Column(name = "Added_on")
	private Date addedOn;
	
	@Column(name = "UPDATED_ON")
	private Date updatedOn;
	
	@Column(name="type")
	private String type;
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="is_featured")
	private Boolean isFeatured;
	
	@Column(name="is_Banner")
	private Boolean isBanner=false;
	
	@Column(name="banner_url")
	private String bannerUrl;

	@Column(name = "attributes", columnDefinition = "json")
	private String attributes;

	@Column(name = "conditions")
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

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Boolean getIsFeatured() {
		return isFeatured;
	}

	public void setIsFeatured(Boolean isFeatured) {
		this.isFeatured = isFeatured;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public ProductCountryRule getProductCountryRule() {
		return productCountryRule;
	}

	public void setProductCountryRule(ProductCountryRule productCountryRule) {
		this.productCountryRule = productCountryRule;
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

	public String getProductSkus() {
		return productSkus;
	}

	public void setProductSkus(String productSkus) {
		this.productSkus = productSkus;
	}

	public Date getAddedOn() {
		return addedOn;
	}

	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
	}

	public Date getLastUpdatedOn() {
		return updatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		updatedOn = lastUpdatedOn;
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
