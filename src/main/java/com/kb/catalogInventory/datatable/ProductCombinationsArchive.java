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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kb.catalogInventory.model.Gender;

@Entity
@Table(name="product_combinations_Archive")
public class ProductCombinationsArchive {
	
	public ProductCombinationsArchive(Product product, String combinationString, String sku, Float price, Float supplierPrice,
			String uniqueIdentifier, Integer availableStock, Date insertedOn, Date updatedOn, String hSN, Float weight,
			Float length, Float width, Float height, Boolean isActive, Integer minOrderQuantity,
			Integer maxOrderQuantity, Float mrp, String productDescription, String searchTerms, ProductStatus status,
			String manufacturerPartNumber, Gender gender, Gender targertGender, String productTaxCode,
			String handlingTime, String countryOfOrigion, String bulletPoints, String targetAudienceKeywords,
			String occasion, String occasionLifeStyle,String itemName,String categoryString,Integer setPieces) {
		super();
		this.product = product;
		this.combinationString = combinationString;
		this.sku = sku;
		this.price = price;
		this.supplierPrice = supplierPrice;
		this.uniqueIdentifier = uniqueIdentifier;
		this.availableStock = availableStock;
		this.insertedOn = insertedOn;
		this.updatedOn = updatedOn;
		HSN = hSN;
		this.weight = weight;
		this.length = length;
		this.width = width;
		this.height = height;
		this.isActive = isActive;
		this.minOrderQuantity = minOrderQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.mrp = mrp;
		this.productDescription = productDescription;
		this.searchTerms = searchTerms;
		this.productStatus = status;
		this.manufacturerPartNumber = manufacturerPartNumber;
		this.gender = gender;
		this.targertGender = targertGender;
		this.productTaxCode = productTaxCode;
		this.handlingTime = handlingTime;
		this.countryOfOrigion = countryOfOrigion;
		this.bulletPoints = bulletPoints;
		this.targetAudienceKeywords = targetAudienceKeywords;
		this.occasion = occasion;
		this.occasionLifeStyle = occasionLifeStyle;
		this.itemName = itemName;
		this.categoryString = categoryString;
		this.setPeices=setPieces;
	}

	

	public ProductCombinationsArchive() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@JsonIgnore
	@JoinColumn(name = "product_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Product product;
	
	@Column(name = "combination_string")
	private String combinationString;
	
	@Column(name = "sku")
	private String sku;
	
	@Column(name = "price")
	private Float price;
	
	@Column(name = "supplier_price")
	private Float supplierPrice;
	
	@Column(name = "unique_identifier")
	private String uniqueIdentifier;
	
	@Column(name = "available_stock")
	private Integer availableStock;

	
	@Column(name = "inserted_on")
	private Date insertedOn;
	
	@Column(name = "updated_on")
	private Date updatedOn;
	
	@Column(name="HSN")
	private String HSN;
	
	@Column(name = "product_weight")
	private Float weight;
	
	@Column(name = "product_length")
	private Float length;
	
	@Column(name = "product_width")
	private Float width;
	
	@Column(name = "product_height")
	private Float height;
	
	@Column(name = "is_active")
	private Boolean isActive;
	
	@Column(name="min_order_quantity")
	private Integer minOrderQuantity;
	
	@Column(name="max_order_quantity")
	private Integer maxOrderQuantity;
	
	@Column(name="mrp")
	private Float mrp;
	
	@Column(name="is_premium")
	private Boolean isPremium;
	
	@Column(name="product_description")
	private String productDescription;
	
	@Column(name="search_terms")
	private String searchTerms;
	
	@JoinColumn(name = "status")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private ProductStatus productStatus;
	
	@Column(name="item_name")
	private String itemName;
	
	@Column(name="category_string")
	private String categoryString;
	
	@JsonIgnore
	@JoinColumn(name = "supplier_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Supplier supplier;
	
	@Column(name="manufacturer_part_number")
	private String manufacturerPartNumber;
	
	@Column(name="gender")
	private Gender gender;
	
	@Column(name="targert_gender")
	private Gender targertGender;
	
	@Column(name="product_tax_code")
	private String productTaxCode;
	
	@Column(name="handling_time")
	private String handlingTime;
	
	@Column(name="country_of_origion")
	private String countryOfOrigion;
	
	@Column(name="bullet_points")
	private String bulletPoints;
	
	@Column(name="target_audience_keywords")
	private String targetAudienceKeywords;
	
	@Column(name="occasion")
	private String occasion;
	
	@Column(name="occasion_life_style")
	private String occasionLifeStyle; 
	
	@Column(name="kb_warehouse_id")
	private Long kbWareHouseId; 
	
	@Column(name="average_rating")
	private Double averageRating;
	
	@Column(name="supplier_currency_id", columnDefinition = " bigint default 1")
	private Long supplierCurrencyId;
	
	@Column(name="comb_str_without_size")
	private String combStrWithoutSize;
	
	
	
	@JoinColumn(name = "country_rule_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private ProductCountryRule productCountryRule;

	
	@Column(name="white_listed_countries")
	private String whiteListedCountries;
	
	@Column(name="black_listed_countries")
	private String blackListedCountries;
	
	@Column(name="collection_Ids")
	private String collectionIds;
	
	@Column(name="set_peices")
	private Integer setPeices;
	
	public String getCollectionIds() {
		return collectionIds;
	}



	public void setCollectionIds(String collectionIds) {
		this.collectionIds = collectionIds;
	}



	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCategoryString() {
		return categoryString;
	}

	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
	}

		
	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

	public ProductStatus getStatus() {
		return productStatus;
	}

	public void setStatus(ProductStatus productStatus) {
		this.productStatus = productStatus;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getMinOrderQuantity() {
		return minOrderQuantity;
	}

	public void setMinOrderQuantity(Integer minOrderQuantity) {
		this.minOrderQuantity = minOrderQuantity;
	}

	public Integer getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Integer maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public Float getMrp() {
		return mrp;
	}

	public void setMrp(Float mrp) {
		this.mrp = mrp;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getCombinationString() {
		return combinationString;
	}

	public void setCombinationString(String combinationString) {
		this.combinationString = combinationString;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}

	public Float getSupplierPrice() {
		return supplierPrice;
	}

	public void setSupplierPrice(Float supplierPrice) {
		this.supplierPrice = supplierPrice;
	}

	public Date getInsertedOn() {
		return insertedOn;
	}

	public void setInsertedOn(Date insertedOn) {
		this.insertedOn = insertedOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getHSN() {
		return HSN;
	}

	public void setHSN(String hSN) {
		HSN = hSN;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public Float getLength() {
		return length;
	}

	public void setLength(Float length) {
		this.length = length;
	}

	public Float getWidth() {
		return width;
	}

	public void setWidth(Float width) {
		this.width = width;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Long getKbWareHouseId() {
		return kbWareHouseId;
	}

	public void setKbWareHouseId(Long kbWareHouseId) {
		this.kbWareHouseId = kbWareHouseId;
	}

	public Boolean getIsPremium() {
		return isPremium;
	}

	public void setIsPremium(Boolean isPremium) {
		this.isPremium = isPremium;
	}
	


	public String getManufacturerPartNumber() {
		return manufacturerPartNumber;
	}

	public void setManufacturerPartNumber(String manufacturerPartNumber) {
		this.manufacturerPartNumber = manufacturerPartNumber;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Gender getTargertGender() {
		return targertGender;
	}

	public void setTargertGender(Gender targertGender) {
		this.targertGender = targertGender;
	}

	public String getProductTaxCode() {
		return productTaxCode;
	}

	public void setProductTaxCode(String productTaxCode) {
		this.productTaxCode = productTaxCode;
	}

	public String getHandlingTime() {
		return handlingTime;
	}

	public void setHandlingTime(String handlingTime) {
		this.handlingTime = handlingTime;
	}

	public String getCountryOfOrigion() {
		return countryOfOrigion;
	}

	public void setCountryOfOrigion(String countryOfOrigion) {
		this.countryOfOrigion = countryOfOrigion;
	}

	public String getBulletPoints() {
		return bulletPoints;
	}

	public void setBulletPoints(String bulletPoints) {
		this.bulletPoints = bulletPoints;
	}

	public String getTargetAudienceKeywords() {
		return targetAudienceKeywords;
	}

	public void setTargetAudienceKeywords(String targetAudienceKeywords) {
		this.targetAudienceKeywords = targetAudienceKeywords;
	}

	public String getOccasion() {
		return occasion;
	}

	public void setOccasion(String occasion) {
		this.occasion = occasion;
	}

	public String getOccasionLifeStyle() {
		return occasionLifeStyle;
	}

	public void setOccasionLifeStyle(String occasionLifeStyle) {
		this.occasionLifeStyle = occasionLifeStyle;
	}
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}



	public Double getAverageRating() {
		return averageRating;
	}



	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}



	public long getSupplierCurrencyId() {
		return supplierCurrencyId;
	}



	public void setSupplierCurrencyId(long supplierCurrencyId) {
		this.supplierCurrencyId = supplierCurrencyId;
	}



	public String getCombStrWithoutSize() {
		return combStrWithoutSize;
	}



	public void setCombStrWithoutSize(String combStrWithoutSize) {
		this.combStrWithoutSize = combStrWithoutSize;
	}
	@JsonProperty
	public String status() {
		return productStatus!=null ?productStatus.getStatusName():null;
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
}
