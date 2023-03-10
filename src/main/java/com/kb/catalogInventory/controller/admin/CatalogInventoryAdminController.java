package com.kb.catalogInventory.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kb.catalogInventory.datatable.LockedItems;
import com.kb.catalogInventory.model.LockItemsRQ;
import com.kb.catalogInventory.repository.ProductCountryRuleRepository;
import com.kb.catalogInventory.service.admin.CatalogInventoryAdminService;
import com.kb.java.utils.RestApiSuccessResponse;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/cataloginventory/admin")
public class CatalogInventoryAdminController {
	
	@Autowired
	private CatalogInventoryAdminService adminservice;
	
	@Autowired
	private ProductCountryRuleRepository countryRuleRepo;
	private final static Logger _logger = LoggerFactory.getLogger(CatalogInventoryAdminController.class);
	
	@GetMapping("/dropdowns")
	public ResponseEntity<?> dropdowns(){
		_logger.info("Inside dropdowns");
		return ResponseEntity.ok(adminservice.productCategoryDropDowns());
	}
	
	@GetMapping("/getCountryRules")
	public ResponseEntity<?> getCountryRules() {
		_logger.info("Inside getCountryRules");
		return ResponseEntity.ok(new RestApiSuccessResponse(HttpStatus.OK.value(),
				"Product Country Rules Fetched Successfully !!", countryRuleRepo.findByIsActiveTrue()));
	}
	
	
	@PutMapping("/updateProdCountryRule/{pcUUID}/{crId}")
	public ResponseEntity<?> updateProdCountryRule(@PathVariable("pcUUID") String pcUUID ,@PathVariable("crId") String crId) {
		_logger.info("Inside updateProdCountryRule");
		return ResponseEntity.ok(new RestApiSuccessResponse(HttpStatus.OK.value(),
				"Product Country Rules Fetched Successfully !!", adminservice.updateProductCountryRule(pcUUID,crId)));
	}

	@PostMapping("/unlockInventoryByBookedOrder")
	public ResponseEntity<?> unlockInventoryByBookedOrder(@RequestBody LockItemsRQ  RQ) {
		_logger.info("Inside unlockInventoryByBookedOrder");
		return ResponseEntity.ok(new RestApiSuccessResponse(HttpStatus.OK.value(),
				"Unlocked Successfully !!", adminservice.unlockInventory(RQ)));
	}

	@PostMapping("/unlockInventoryByOrderAttempt")
	public ResponseEntity<?> unlockInventoryByOrderAttempt(@RequestBody Map RQ) {
		_logger.info("Inside unlockInventoryByOrderAttempt");
		return ResponseEntity.ok(new RestApiSuccessResponse(HttpStatus.OK.value(),
				"Unlocked Successfully !!", adminservice.unlockInventoryByOrderAttempt(RQ)));
	}


	@PostMapping("/getSupplierIdFromProducts")
	public ResponseEntity<?> getSupplierIdFromProducts(@RequestBody List<String> RQ) {
		_logger.info("Inside getSupplierIdFromProducts");
		return ResponseEntity.ok(new RestApiSuccessResponse(HttpStatus.OK.value(),
				"Fetched Successfully !!", adminservice.getSupplierIdsFromProducts(RQ)));
	}



	@PostMapping("/getSupplierEmailsFromIds")
	public ResponseEntity<?> getSupplierEmailsFromIds(@RequestBody List<Long> supplierIds) {
		_logger.info("Inside getSupplierIdFromProducts");
		return ResponseEntity.ok(new RestApiSuccessResponse(HttpStatus.OK.value(),
				"Fetched Successfully !!", adminservice.getSupplierEmailsFromIds(supplierIds)));
	}


	@PostMapping("/getHsnOfProducts")
	public ResponseEntity<?> getHsnOfProducts(@RequestBody List<String> supplierIds) {
		_logger.info("Inside getSupplierIdFromProducts");
		return ResponseEntity.ok(new RestApiSuccessResponse(HttpStatus.OK.value(),
				"Fetched Successfully !!", adminservice.getHsnOfProducts(supplierIds)));
	}


}
