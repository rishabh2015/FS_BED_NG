package com.kb.catalogInventory.datatable;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import com.kb.catalogInventory.model.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Immutable
@Subselect("select * from product_view")
public class ProductView {

	@Column(name = "pc_uuid")
	private String pcUuid;
	@Id
	@Column(name = "pc_id")
	private String pcId;
	@Column(name = "kb_warehouse_id")
	private Long kbwarehouseid;
	@Column(name = "hsn")
	private String hsn;
	@Column(name = "product_weight")
	private Float productWeight;
	@Column(name = "product_length")
	private Float productLength;
	@Column(name = "product_width")
	private Float productWidth;
	@Column(name = "product_height")
	private Float productHeight;
	@Column(name = "inserted_on")
	private Date createdOn;
	@Column(name = "combination_string")
	private String combinationString;
	@Column(name = "status_name")
	private String status;
	@Column(name = "status_id")
	private Long statusId;
	@Column(name = "price")
	private Float price;
	@Column(name = "supplier_price")
	private Float supplierPrice;
	@Column(name = "available_stock")
	private Integer availableStock;
	@Column(name = "is_active")
	private Boolean isActive;
	@Column(name = "max_order_quantity")
	private Integer maxOrderQuantity;
	@Column(name = "min_order_quantity")
	private Integer minOrderQuantity;
	@Column(name = "mrp")
	private Float mrp;
	@Column(name = "product_description")
	private String productDescription;
	@Column(name = "search_terms")
	private String searchTerms;
	@Column(name = "category_string")
	private String categoryString;
	@Column(name = "manufacturer_part_number")
	private String manufacturerPartNumber;
	@Column(name = "gender")
	private Gender gender;
	@Column(name = "targert_gender")
	private Gender targertGender;
	@Column(name = "product_tax_code")
	private String productTaxCode;
	@Column(name = "handling_time")
	private String handlingTime;
	@Column(name = "country_of_origion")
	private String countryOfOrigion;
	@Column(name = "bullet_points")
	private String bulletPoints;
	@Column(name = "target_audience_keywords")
	private String targetAudienceKeywords;
	@Column(name = "occasion")
	private String occasion;
	@Column(name = "occasion_life_style")
	private String occasionLifeStyle;
	@Column(name = "sku")
	private String sku;
	@Column(name = "item_name")
	private String itemName;
	@Column(name = "product_sku")
	private String productSku;
	@Column(name = "uuid")
	private String productUuid;
	@Column(name = "bmc_name")
	private String bmcName;
	@Column(name = "bm_name")
	private String bmName;
	@Column(name = "b_name")
	private String brandName;
	@Column(name = "p_id")
	private Long productId;
	@Column(name = "s_id")
	private Long supplierId;
	@Column(name = "s_name")
	private String supplierName;
	@Column(name = "s_address")
	private String suppAddress;
	@Column(name = "s_pin_code")
	private String suppPinCode;
	@Column(name = "lowest_category")
	private Long lowestCategory;
	@Column(name = "legal_disclaimer_description")
	private String legalDisclaimerDescription;
	@Column(name = "manufacturer_contact_number")
	private String manufacturerContactNumber;
	@Column(name = "product_string")
	private String productString;
	@Column(name = "product_name")
	private String productName;
	@Column(name = "bm_id")
	private Long bmId;
	@Column(name = "supp_active")
	private boolean isActiveSupp;
	@Column(name = "reseller_discount")
	private Long resellerDiscount;
	@Column(name = "is_premium")
	private boolean isPremium;
	@Column(name = "average_rating")
	private Float averageRating;
	
	@Column(name = "supplier_currency_id")
	private Long supplierCurrencyId;
	
	@Column(name="comb_str_without_size")
	private String combStrWithoutSize;
	
	@Column(name="lowest_cat_name")
	private String lowestCategoryName;

	@Column(name="s_email")
	private String supplierEmail;
	/**
	 * pcr- product country rule
	 * wlc - white listed country
	 * blc- black listed countries
	 * pcr_da- product country rule display all
	 */
	
	@Column(name="product_wlc")
	private String productWlc;
	
	@Column(name="product_blc")
	private String productBlc;
		
	@Column(name="pcr_id")
	private Long pcrId;
	
	@Column(name="pcr_wlc")
	private String pcrWlc;
	
	
	@Column(name="pcr_da")
	private Boolean pcrDa;
	
	@Column(name="pcr_inter")
	private Boolean pcrInter;
	
	@Column(name="pcr_zone")
	private String pcrZone;
	
	@Column(name="b_id")
	private Long brandId;
	
	@Column(name="bmc_id")
	private Long brandModelCategoryId;
	
	@Column(name="pc_collection_id")
	private String collectionIds;
	
	@Column(name="b_logo")
	private String brandICon;
	
	@Column(name="pc_set_peices")
	private Integer setPieces;
	
	@Column(name="b_is_featured")
	private Boolean brandIsFeatured;
	
	@Column(name="b_sort_order")
	private Long brandSortOrder;

	@Column(name = "b_same_manufturer_as")
	private String brandSameManufacturerAs;

	@Column(name = "pc_display_price")
	private Float displayPrice;

	@Column(name = "supplier_mov")
	private BigDecimal supplierMov;
	
	@Column(name = "is_supplier_mov_applicable")
	private Boolean isSupplierMovApplicable;




}
