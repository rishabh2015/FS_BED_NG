package com.kb.catalogInventory.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kb.catalogInventory.datatable.Supplier;
import com.kb.catalogInventory.model.SupplierBo;

import java.util.Date;

@Component
public class SupplierMapper {


	public SupplierBo convertSupplierToBo(Supplier supplier) {
		SupplierBo supplierBo=new SupplierBo();
		supplierBo.setIsSupplierMovApplicable(supplier.getSupplierMovApplicable());
		supplierBo.setSupplierDiscountPercentage(supplier.getSupplierDiscountPercentage());
		supplierBo.setMovPrice(supplier.getMovPrice());
		supplierBo.setDiscountExpDate(supplier.getDiscountExpDate());
		supplierBo.setIsSupplierAdditionDiscountApplicable(supplier.getSupplierAdditionDiscountApplicable());
		supplierBo.setId(supplier.getId());
		supplierBo.setSupplierName(supplier.getSupplierName());
		supplierBo.setSupplierAddress(supplier.getSupplierAddress());
		supplierBo.setGstOrUdyogNumber(supplier.getGstOrUdyogNumber());
		supplierBo.setAddressType(supplier.getAddressType());
		supplierBo.setEmail(supplier.getEmail());
		supplierBo.setPhone(supplier.getPhone());
		supplierBo.setAddress2(supplier.getAddress2());
		supplierBo.setCity(supplier.getCity());
		supplierBo.setState(supplier.getState());
		supplierBo.setCountry(supplier.getCountry());
		supplierBo.setPin_code(supplier.getPin_code());
		supplierBo.setGender(supplier.getGender());
		if(supplier.getCreatedOn()!=null) {
			supplierBo.setCreatedOn(supplier.getCreatedOn().getTime());
		}
		supplierBo.setActive(supplier.isActive());;
		if(supplier.getUpdatedOn()!=null) {
			supplierBo.setUpdatedOn(supplier.getUpdatedOn().getTime());
		}
		supplierBo.setDirectShipment(supplier.isDirectShipment());;
		supplierBo.setFirstName(supplier.getFirstName());;
		supplierBo.setLastName(supplier.getLastName());;
		supplierBo.setAvgHandlingTimePerProd(supplier.getAvgHandlingTimePerProd());
		supplierBo.setShippingPickUpLocId(supplier.getShippingPickUpLocId());
		supplierBo.setIsKbPickup(supplier.getKbPickup());
		supplierBo.setLegalName(supplier.getLegalname());

		return supplierBo;
	}
}
