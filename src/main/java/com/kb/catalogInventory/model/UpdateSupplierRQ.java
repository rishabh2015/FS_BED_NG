package com.kb.catalogInventory.model;

import javax.persistence.Column;
import java.util.List;

public class UpdateSupplierRQ {

    private List<GroupsBo> groupList;


    private String supplierName;
    private String supplierAddress;
    private String gstOrUdyogNumber;
    private String addressType;
    private String email;
    private String phone;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String pinCode;
    private Gender gender;
    private String firstName;
    private String lastName;
    private String legalnm;
    private String shipmentPickUpLocationId;
    private Boolean isKbPickup;
    private Boolean isCustomSupplierInvoiceNum;

    public String getLegalnm() {
        return legalnm;
    }

    public void setLegalnm(String legalnm) {
        this.legalnm = legalnm;
    }

    public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
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

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
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

	public String getShipmentPickUpLocationId() {
		return shipmentPickUpLocationId;
	}

	public void setShipmentPickUpLocationId(String shipmentPickUpLocationId) {
		this.shipmentPickUpLocationId = shipmentPickUpLocationId;
	}

	public Boolean getIsKbPickup() {
		return isKbPickup;
	}

	public void setIsKbPickup(Boolean isKbPickup) {
		this.isKbPickup = isKbPickup;
	}
    public List<GroupsBo> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupsBo> groupList) {
        this.groupList = groupList;
    }

    public Boolean getIsCustomSupplierInvoiceNum() {
        return isCustomSupplierInvoiceNum;
    }

    public void setIsCustomSupplierInvoiceNum(Boolean customSupplierInvoiceNum) {
        isCustomSupplierInvoiceNum = customSupplierInvoiceNum;
    }
}
