package com.kb.catalogInventory.controller;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kb.catalogInventory.exception.ControllerExceptionHandler;
import com.kb.catalogInventory.exception.InventoryException;
import com.kb.catalogInventory.model.FinalPriceAndSupplierDetailModel;
import com.kb.catalogInventory.service.ProductDetailService;

@RestController
@CrossOrigin
@RequestMapping("/productPricing")

@Validated
public class ProductPricingController extends ControllerExceptionHandler{
	
	@Autowired
	private ProductDetailService productDetailDService;
	
	private final static Logger _logger = LoggerFactory.getLogger(ProductDetailService.class);
	
	@GetMapping("/basePricePerUnit/{UUID}")
	public ResponseEntity<?> getBasePricePerUnit(
			@PathVariable("UUID") @NotBlank @Size(min=32,message="Not a Valid Unique Identifier") String UUID){
		_logger.info("Entered getBasePricePerUnit with UUID : {}",UUID);
		Object obj = productDetailDService.getBasePricePerUnit(UUID);
		_logger.debug("Leaving getBasePricePerUnit properties : {}",obj);
		return ResponseEntity.ok(obj);
	}
	
	@GetMapping("/basePricePerUnitCurr/{UUID}")
	public ResponseEntity<?> getBasePricePerUnitCurr(
			@RequestHeader(value = "countrycode", required = false) @NotBlank(message = "Country code missing") @Size(min = 2,max = 2, message = "Not a valid country code") String countryCode,
			@PathVariable("UUID") @NotBlank @Size(min=32,message="Not a Valid Unique Identifier") String UUID) throws InventoryException{
		_logger.info("Entered getBasePricePerUnit with UUID : {}",UUID);
		FinalPriceAndSupplierDetailModel obj = productDetailDService.getBasePricePerUnitCurr(UUID,countryCode);
		_logger.debug("Leaving getBasePricePerUnit properties : {}",obj);
		return ResponseEntity.ok(obj);
	}
	
	@GetMapping("/supplierBasePricePerUnit/{UUID}")
	public ResponseEntity<?> getSupplierBasePricePerUnit(
			@PathVariable("UUID") @NotBlank @Size(min=32,message="Not a Valid Unique Identifier") String UUID){
		_logger.info("Entered getBasePricePerUnit with UUID : {}",UUID);
		Object obj =null;
		try {
			obj=	productDetailDService.getSupplierBasePricePerUnit(UUID);
		}catch (Exception e){
			return ResponseEntity.ok(obj);
		}

		return ResponseEntity.ok(obj);
	}

	@PostMapping("/updateProductCombinationBasedOnSupplierId")
	public ResponseEntity<?> updateProductCombinationBasedOnSupplierId(@RequestBody List<Map<String,List<String>>> kbMarginUpdatedMapList){
		_logger.info("Going to update Product Combination updated_on column based on supplier id/productid/lowest category name for the updated kb margin");
		return ResponseEntity.ok(productDetailDService.updateProductCombinationBasedonUpdatedKbMrgin(kbMarginUpdatedMapList));
	}
	
}
