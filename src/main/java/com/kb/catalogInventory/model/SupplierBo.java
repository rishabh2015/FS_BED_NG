package com.kb.catalogInventory.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
public class SupplierBo {
	private long id;
	private String supplierName;
	private String supplierAddress;
	private String gstOrUdyogNumber;
    private String addressType;
	private String  email;
	private String  phone;
	private String  address2;
	private String  city;
	private String  state;
	private String  country;
	private String  pin_code;
	private Gender  gender;
	private Long createdOn;
	private boolean isActive;
	private Long updatedOn;
	private boolean directShipment;
    private String firstName;
	private String lastName;
	private String avgHandlingTimePerProd;
	private String shippingPickUpLocId;
	private Boolean isKbPickup;
	private List<BrandBO> brandBOList;

	/*private String brandLogo;
	private String brandName;*/
	private String legalName;
	/*private Float avgRatting;*/
	private List<GroupsBo> groups;

	private Boolean isSupplierMovApplicable;
	private Double movPrice;
	private Boolean isSupplierAdditionDiscountApplicable;
	private Double supplierDiscountPercentage;
	private Date discountExpDate;
	private Boolean isMovSuccess;
	private String  movErrormsg;
}
