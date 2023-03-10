package com.kb.catalogInventory.model;

import java.util.List;
import java.util.Map;

import com.kb.catalogInventory.datatable.ProductStatus;

public class ProductInventoryRQ1 {
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	private String categoryString;
	private String brandModelString;

	private String variationVariationOptionString;
	private String supplierString;
	private String supplierDetail;
	private String totalStock;

	private String imageNameString;
	private String productString;
	private String productNameString;



	private String SKU;
	private String parentSku;
	private String supplierAmount;

	private List<ColorSizeCombination> colorSizeCombinationList;


	private String productId;//product
	private String manufacturerPartNumber;//discuss  //productCombination
	private Gender gender;//discuss  //productCombination
	private Gender targertGender;//discuss  //productCombination
	private String productTaxCode;//discuss //productCombination
	private Double mrp; //productCombination
	private float saleDiscountPrice; //discuss(pricing service or inventory)
	private String saleStartDate; //discuss(pricing service or inventory)
	private String saleEndDate; //discuss(pricing service or inventory)
	private int minOrderQuantity; //productCombination
	private int maxOrderQuantity; //productCombination
	private String handlingTime; //discuss //productCombination
	private String countryOfOrigion; //discuss //productCombination
	private String productDescription;//productCombination
	private String bulletPoints; //discuss //productCombination
	private String legalDisclaimerDescription; //Product
	private String targetAudienceKeywords; //discuss //productCombination
	private String searchTerms; //productCombination
	private Float weight; //productCombination
	private Float length; //productCombination
	private Float width; //productCombination
	private Float height; //productCombination
	private String occasion; //discuss  //productCombination
	private String occasionLifeStyle; //discuss //productCombination
	private String manufacturerContactNumber; //Product
	private String itemName; //Product
	private String brandName; //brand
	private List<Map<String,String>> priceMatrix;
	private String oldStock;
	private long brandModelId;
	private String productCombinationUUID;
	private Boolean isPremium;
	private String productSKU;
	private ProductStatus status;
	
	
	private String image1;
	
	private String image2;
	
	private String image3;
	
	private String image4;
	
	private String image5;
	
	private String image6;
	
	private String image7;
	
	private String image8;
	
	private String image9;
	
	private long supplierCurrencyId;
	
	private long supplierId;
	
	private Long productContryRuleId;
	
	private Integer setPeices;

	private String hsnCode;

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public String getProductAttribute() {
		return productAttribute;
	}

	public void setProductAttribute(String productAttribute) {
		this.productAttribute = productAttribute;
	}

	private String productAttribute;

	public List<ColorSizeCombination> getColorSizeCombinationList() {
		return colorSizeCombinationList;
	}

	public void setColorSizeCombinationList(List<ColorSizeCombination> colorSizeCombinationList) {
		this.colorSizeCombinationList = colorSizeCombinationList;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public Integer getSetPeices() {
		return setPeices;
	}

	public void setSetPeices(Integer setPeices) {
		this.setPeices = setPeices;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public String getImage4() {
		return image4;
	}

	public void setImage4(String image4) {
		this.image4 = image4;
	}

	public String getImage5() {
		return image5;
	}

	public void setImage5(String image5) {
		this.image5 = image5;
	}

	public String getImage6() {
		return image6;
	}

	public void setImage6(String image6) {
		this.image6 = image6;
	}

	public String getImage7() {
		return image7;
	}

	public void setImage7(String image7) {
		this.image7 = image7;
	}

	public String getImage8() {
		return image8;
	}

	public void setImage8(String image8) {
		this.image8 = image8;
	}

	public String getImage9() {
		return image9;
	}

	public void setImage9(String image9) {
		this.image9 = image9;
	}

	public long getBrandModelId() {
		return brandModelId;
	}

	public void setBrandModelId(long brandModelId) {
		this.brandModelId = brandModelId;
	}

	public List<Map<String, String>> getPriceMatrix() {
		return priceMatrix;
	}

	public void setPriceMatrix(List<Map<String, String>> priceMatrix) {
		this.priceMatrix = priceMatrix;
	}

	public String getOldStock() {
		return oldStock;
	}

	public void setOldStock(String oldStock) {
		this.oldStock = oldStock;
	}
	
	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}

	public String getCategoryString() {
		return categoryString;
	}

	public void setCategoryString(String categoryString) {
		this.categoryString = categoryString;
	}

	public String getBrandModelString() {
		return brandModelString;
	}

	public void setBrandModelString(String brandModelString) {
		this.brandModelString = brandModelString;
	}

	public String getVariationVariationOptionString() {
		return variationVariationOptionString;
	}

	public void setVariationVariationOptionString(String variationVariationOptionString) {
		this.variationVariationOptionString = variationVariationOptionString;
	}

	public String getSupplierString() {
		return supplierString;
	}

	public void setSupplierString(String supplierString) {
		this.supplierString = supplierString;
	}

	public String getSupplierDetail() {
		return supplierDetail;
	}

	public void setSupplierDetail(String supplierDetail) {
		this.supplierDetail = supplierDetail;
	}

	public String getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(String totalStock) {
		this.totalStock = totalStock;
	}

	public String getImageNameString() {
		return imageNameString;
	}

	public void setImageNameString(String imageNameString) {
		this.imageNameString = imageNameString;
	}

	public String getProductString() {
		return productString;
	}

	public void setProductString(String productString) {
		this.productString = productString;
	}

	public String getProductNameString() {
		return productNameString;
	}

	public void setProductNameString(String productNameString) {
		this.productNameString = productNameString;
	}

	public String getSKU() {
		return SKU;
	}

	public void setSKU(String sKU) {
		SKU = sKU;
	}

	public String getSupplierAmount() {
		return supplierAmount;
	}

	public void setSupplierAmount(String supplierAmount) {
		this.supplierAmount = supplierAmount;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public float getSaleDiscountPrice() {
		return saleDiscountPrice;
	}

	public void setSaleDiscountPrice(float saleDiscountPrice) {
		this.saleDiscountPrice = saleDiscountPrice;
	}

	public String getSaleStartDate() {
		return saleStartDate;
	}

	public void setSaleStartDate(String saleStartDate) {
		this.saleStartDate = saleStartDate;
	}

	public String getSaleEndDate() {
		return saleEndDate;
	}

	public void setSaleEndDate(String saleEndDate) {
		this.saleEndDate = saleEndDate;
	}

	public int getMinOrderQuantity() {
		return minOrderQuantity;
	}

	public void setMinOrderQuantity(int minOrderQuantity) {
		this.minOrderQuantity = minOrderQuantity;
	}

	public int getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(int maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
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

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getBulletPoints() {
		return bulletPoints;
	}

	public void setBulletPoints(String bulletPoints) {
		this.bulletPoints = bulletPoints;
	}

	public String getLegalDisclaimerDescription() {
		return legalDisclaimerDescription;
	}

	public void setLegalDisclaimerDescription(String legalDisclaimerDescription) {
		this.legalDisclaimerDescription = legalDisclaimerDescription;
	}

	public String getTargetAudienceKeywords() {
		return targetAudienceKeywords;
	}

	public void setTargetAudienceKeywords(String targetAudienceKeywords) {
		this.targetAudienceKeywords = targetAudienceKeywords;
	}

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
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

	public String getManufacturerContactNumber() {
		return manufacturerContactNumber;
	}

	public void setManufacturerContactNumber(String manufacturerContactNumber) {
		this.manufacturerContactNumber = manufacturerContactNumber;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}


	public String getProductCombinationUUID() {
		return productCombinationUUID;
	}

	public void setProductCombinationUUID(String productCombinationUUID) {
		this.productCombinationUUID = productCombinationUUID;
	}

	public Boolean getIsPremium() {
		return isPremium;
	}

	public void setIsPremium(Boolean isPremium) {
		this.isPremium = isPremium;
	}

	public String getProductSKU() {
		return productSKU;
	}

	public void setProductSKU(String productSKU) {
		this.productSKU = productSKU;
	}

	public long getSupplierCurrencyId() {
		return supplierCurrencyId;
	}

	public void setSupplierCurrencyId(long supplierCurrencyId) {
		this.supplierCurrencyId = supplierCurrencyId;
	}

	public Long getProductContryRuleId() {
		return productContryRuleId;
	}

	public void setProductContryRuleId(Long productContryRuleId) {
		this.productContryRuleId = productContryRuleId;
	}

	
	
	
}
