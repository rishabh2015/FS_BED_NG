package com.kb.catalogInventory.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Data
public class ProductInventoryBO implements Serializable{
	
	/**
	 * 
	 */
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	
	
	private String categoryName;
	private String userPhoneString;
	private String categoryId;
	private  Map<String, Map<String, String>> categoryList;
	private String brands;
	private String  brandModel;
	private String  SKU;
	private String  name;
	private String  productURL;
	private String  currency;
	@JsonIgnore
	private String displayPrice;
	private String displayCurrency; //Added new
	private Map<String,String> combination;
	private String  uniqueIdentifierForSearch;
	private List<ImagesBO> images;
	private Integer totalImageCount;
	private String description;
	private String thumbnailURL;
	private String badges;
	private String ratingAvg;
	private String ratingCount;
	private String availabilityCount;
	private String productClubingId;
	private String sellerName;
	private String sellerAddress;
	private String createdOn;
	private String sellerCountry;
	private String HSN;
	private Float weight;
	private Float length;
	private Float width;
	private Float height;
	private String pickUpPincode;
	private long sellerId;
	private boolean inStock;
	private boolean isActive;
	private List<ProductPriceMatrix> priceMatrix;
	private String status;
	
	private String legalDisclaimerDescription;
	private String manufacturerContactNumber;
	private Integer minOrderQuantity;
	private Integer maxOrderQuantity;
	@JsonIgnore
	private Float mrp;
	private String productDescription;
	private String searchTerms;
	private String categoryString;
	private String itemName;
	private String brandName;
	private String manufacturerPartNumber;
	private Gender gender;
	private Integer totalStock;
	private Gender targertGender;
	private String variationVariationOptionString;
	private String productTaxCode;
	private Float supplierAmount;
	private Float saleDiscountPrice;
	private String saleStartDate;
	private String saleEndDate;
	private String handlingTime; 
	private String countryOfOrigion;
	private List<String> bulletPoints;
	private String targetAudienceKeywords;
	private String occasion;
	private String occasionLifeStyle;
	private String imageNameString;
	private String supplierString;
	private String brandModelString;
	private String supplierDetail;
	private String productString;
	private String productNameString;
	private long brandModelId;
	private long wareHouseId;
	private String productSKU;
	private Long resellerDiscount;
	private boolean isPremiumProduct;
	private long defaultCurrency;
	private long displayCurrencyId;
	private String errMessage;
	private String combStrWithoutSize;
	private List<Map<String,Object>> priceMatrixMapList;
	private String supplierSignature;
	@JsonIgnore
	private Double priceAfterDiscount;
	private String countryRuleName;
	private String whiteListedCountries;
	private Long countryRuleId;
	private String collectionIds;
	private Long brandId;
	private String brandIcon;
	private Long supplierId;
	@JsonIgnore
	private Integer setPeices;
	@JsonIgnore
	private String setPrice;
	@JsonIgnore
	private Double discountPercentPerUnit;
	@JsonIgnore
	private Double totalTax;
	@JsonIgnore
	private Double totalDiscount;
	private Map<String, ProductPrice> countryWisePrice;
	@JsonIgnore
	private Double discountedSetPricePerPeiece;
	@JsonIgnore
	private boolean isSlashed=false;
	private String basedIn;
	private double averageBrandRating;
	private Integer totalBrandReviews;
	private String priceBeforeTax;
	private Double taxPercentage;
	private Boolean brandIsFeatured;
	private Long brandSortOrder;
	private ProductPrice productPrice;
	private String brandSameManufacturerAs;
	private Long bestSellingProductCount;
	private List<ColorSizeCombination> colorSizeCombinationList;
	private Boolean isUserLoginRequired;
	private Boolean isPremiumBrand;
	private Boolean isUserBelongToBrand;
	private ProductAttributeBo productAttribute;
	private BigDecimal supplierMov;
	private Boolean isSupplierMovApplicabe;

}
