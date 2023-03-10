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
@Table(name="product")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "product_name")
	private String productName;
	
	@Column(name = "product_string")
	private String productString;
	
	@Column(name = "preview_image")
	private String previewImage;
	
	

	public Product() {
	}
	@Column(name = "country_id")
	private long countryId;
	
	@JoinColumn(name = "brand_model_category_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private BrandModelCategory brandModelCategory;
	
	@JoinColumn(name = "supplier_id")
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Supplier supplier;
	
	
	@Column(name = "updated_on")
	private Date updatedOn;
	
	@Column(name="UUID")
	private String UUID;
	
	@Column(name="legal_disclaimer_description")
	private String legalDisclaimerDescription;
	
	@Column(name="manufacturer_contact_number")
	private String manufacturerContactNumber;
	
	@Column(name="product_sku")
	private String productSku;
	

	public String getLegalDisclaimerDescription() {
		return legalDisclaimerDescription;
	}

	public void setLegalDisclaimerDescription(String legalDisclaimerDescription) {
		this.legalDisclaimerDescription = legalDisclaimerDescription;
	}

	public String getManufacturerContactNumber() {
		return manufacturerContactNumber;
	}

	public void setManufacturerContactNumber(String manufacturerContactNumber) {
		this.manufacturerContactNumber = manufacturerContactNumber;
	}

	public Product(String productName, String productString, String previewImage, long countryId,
			BrandModelCategory brandModelCategory, Supplier supplier, Date updatedOn,String legalDisclaimerDescription,String manufacturerContactNumber,String UUID,String productSku) {
		super();
		this.productName = productName;
		this.productString = productString;
		this.previewImage = previewImage;
		this.countryId = countryId;
		this.brandModelCategory = brandModelCategory;
		this.supplier = supplier;
		this.updatedOn = updatedOn;
		this.legalDisclaimerDescription = legalDisclaimerDescription;
		this.manufacturerContactNumber = manufacturerContactNumber;
		this.UUID = UUID;
		this.productSku=productSku;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductString() {
		return productString;
	}

	public void setProductString(String productString) {
		this.productString = productString;
	}

	public String getPreviewImage() {
		return previewImage;
	}

	public void setPreviewImage(String previewImage) {
		this.previewImage = previewImage;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public BrandModelCategory getBrandModelCategory() {
		return brandModelCategory;
	}

	public void setBrandModelCategory(BrandModelCategory brandModelCategory) {
		this.brandModelCategory = brandModelCategory;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}
	
}
