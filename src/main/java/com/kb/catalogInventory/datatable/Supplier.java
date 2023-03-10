package com.kb.catalogInventory.datatable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.kb.catalogInventory.model.Gender;

@Entity
@Table(name="supplier")
public class Supplier {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "supplier_name")
	private String supplierName;
	@Column(name = "supplier_address")
	private String supplierAddress;
	@Column(name = "gst_or_udyog_number")
	private String gstOrUdyogNumber;
	@Column(name = "supplier_address_type")
    private String addressType;
	@Column(name = "supplier_email")
	private String  email;
	@Column(name = "supplier_phone")
	private String  phone;
	@Column(name = "supplier_address_2")
	private String  address2;
	private String  city;
	private String  state;
	private String  country;
	private String  pin_code;
	private Gender  gender;
	@Column(name = "created_on")
	private Date createdOn;
	@Column(name = "is_active")
	private boolean isActive;
	@Column(name = "updated_on")
	private Date updatedOn;
	@Column(name = "is_direct_shippment" ,columnDefinition = "tinyint(1) default 1")
	private boolean directShipment;

    @Column(name = "first_name")
    private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "legal_name")
	private String legalname;


	@Column(name = "avg_handling_time_per_prod")
	private String avgHandlingTimePerProd;
	
	@Column(name = "shipping_pickup_loc_id")
	private String shippingPickUpLocId;
	
	@Column(name = "kb_pick_up")
	private Boolean isKbPickup;

	@Column(name = "group_string")
	private String groupString;

	@Column(name = "is_custom_supplier_invoice_no")
	private Boolean isCustomSupplierInvoiceNum;

	@Column (name = "is_supplier_mov_applicable")
	private Boolean isSupplierMovApplicable;

	@Column(name = "mov_price")
	private Double movPrice;
   @Column(name = "is_supplier_additional_discount_applicable")
   private Boolean isSupplierAdditionDiscountApplicable;
   @Column(name = "supplier_discount_percentage")
   private Double supplierDiscountPercentage;

   @Column(name = "discount_expiry_date")
   private Date discountExpDate;

	public Boolean getSupplierMovApplicable() {
		return isSupplierMovApplicable;
	}

	public void setSupplierMovApplicable(Boolean supplierMovApplicable) {
		isSupplierMovApplicable = supplierMovApplicable;
	}

	public Double getMovPrice() {
		return movPrice;
	}

	public void setMovPrice(Double movPrice) {
		this.movPrice = movPrice;
	}

	public Boolean getSupplierAdditionDiscountApplicable() {
		return isSupplierAdditionDiscountApplicable;
	}

	public void setSupplierAdditionDiscountApplicable(Boolean supplierAdditionDiscountApplicable) {
		isSupplierAdditionDiscountApplicable = supplierAdditionDiscountApplicable;
	}

	public Double getSupplierDiscountPercentage() {
		return supplierDiscountPercentage;
	}

	public void setSupplierDiscountPercentage(Double supplierDiscountPercentage) {
		this.supplierDiscountPercentage = supplierDiscountPercentage;
	}

	public Date getDiscountExpDate() {
		return discountExpDate;
	}

	public void setDiscountExpDate(Date discountExpDate) {
		this.discountExpDate = discountExpDate;
	}

	public String getGroupString() {
		return groupString;
	}

	public void setGroupString(String groupString) {
		this.groupString = groupString;
	}

	public Boolean getKbPickup() {
		return isKbPickup;
	}

	public void setKbPickup(Boolean kbPickup) {
		isKbPickup = kbPickup;
	}

	public Supplier(){
		
	}

	

	public Supplier(String supplierName, String supplierAddress, String gstOrUdyogNumber) {
		super();
		this.supplierName = supplierName;
		this.supplierAddress = supplierAddress;
		this.gstOrUdyogNumber = gstOrUdyogNumber;
	}
	
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}

	public String getGstOrUdyogNumber() {
		return gstOrUdyogNumber;
	}

	public void setGstOrUdyogNumber(String gstOrUdyogNumber) {
		this.gstOrUdyogNumber = gstOrUdyogNumber;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public String getLegalname() {
		return legalname;
	}

	public void setLegalname(String legalname) {
		this.legalname = legalname;
	}

	public String getAddressType() {
		return addressType;
	}



	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}



	public String getAddress2() {
		return address2;
	}



	public void setAddress2(String address2) {
		this.address2 = address2;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getPin_code() {
		return pin_code;
	}



	public void setPin_code(String pin_code) {
		this.pin_code = pin_code;
	}



	public Gender getGender() {
		return gender;
	}



	public void setGender(Gender gender) {
		this.gender = gender;
	}



	public Date getCreatedOn() {
		return createdOn;
	}



	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}



	public boolean isActive() {
		return isActive;
	}



	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}



	public Date getUpdatedOn() {
		return updatedOn;
	}



	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}



	public boolean isDirectShipment() {
		return directShipment;
	}



	public void setDirectShipment(boolean directShipment) {
		this.directShipment = directShipment;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public String getAvgHandlingTimePerProd() {
		return avgHandlingTimePerProd;
	}



	public void setAvgHandlingTimePerProd(String avgHandlingTimePerProd) {
		this.avgHandlingTimePerProd = avgHandlingTimePerProd;
	}

	public String getShippingPickUpLocId() {
		return shippingPickUpLocId;
	}

	public void setShippingPickUpLocId(String shippingPickUpLocId) {
		this.shippingPickUpLocId = shippingPickUpLocId;
	}

	public Boolean getIsCustomSupplierInvoiceNum() {
		return isCustomSupplierInvoiceNum;
	}

	public void setIsCustomSupplierInvoiceNum(Boolean customSupplierInvoiceNum) {
		isCustomSupplierInvoiceNum = customSupplierInvoiceNum;
	}
}
