package com.kb.catalogInventory.controller.admin;

import com.kb.catalogInventory.model.BulkPushByProductIdsRQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kb.catalogInventory.controller.ProductDetailController;
import com.kb.catalogInventory.service.admin.BulkUpdateAdminService;

@RequestMapping("/bulkUpdate")
@RestController
@CrossOrigin
public class BulkUpdateController {

	@Autowired
	private BulkUpdateAdminService bulkservice;

	private final static Logger _logger = LoggerFactory.getLogger(BulkUpdateController.class);


	@GetMapping("/productCombinationUuids")
	public ResponseEntity<?> updateUuuidsOfProductCombination() {
		_logger.info("Inside updateUuuidsOfProductCombination of BulkUpdateController ");

		bulkservice.updateAllUuidsOfProductCombination();
		return ResponseEntity.ok("completed");
	}

	@GetMapping("/bulkPushInventories")
	public ResponseEntity<?> bulkPushInventories(@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode) {

		_logger.info("Inside bulkPushInventories of BulkUpdateController with displayCountryCode:: {}",displayCountryCode);
		String displayCurrencyCode=new ProductDetailController().getDefaultCurrency(displayCountryCode);
		bulkservice.bulkPushInventoriesToKafkaWithMuliThread(displayCountryCode,displayCurrencyCode);
		return ResponseEntity.ok("completed");
	}
	
	@GetMapping("/productUuids")
	public ResponseEntity<?> updateUuuidsOfProduct() {
		_logger.info("Inside updateUuuidsOfProduct of BulkUpdateController");
		bulkservice.updateAllUuidsOfProduct();
		return ResponseEntity.ok("completed");
	}

	@GetMapping("/pushOneProductToKafka/{uuid}")
	public ResponseEntity<?> PushInventory(@PathVariable ("uuid") String uuid,@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode) {

		_logger.info("Inside PushInventory of BulkUpdateController with displayCountryCode:: {} uuid:: {}",displayCountryCode ,uuid);
		String displayCurrencyCode=new ProductDetailController().getDefaultCurrency(displayCountryCode);
		return ResponseEntity.ok(bulkservice.pushProductToKafka(uuid,displayCountryCode,displayCurrencyCode));
	}

	@PostMapping("/pushProductsToKafka")
	public ResponseEntity<?> PushInventory(@RequestBody BulkPushByProductIdsRQ request, @RequestParam(value = "displayCountryCode", required = false) String displayCountryCode) throws Exception {

		_logger.info("Inside PushInventory of BulkUpdateController with displayCountryCode:: {} uuids:: {}",displayCountryCode ,request.getProductIds());
		String displayCurrencyCode=new ProductDetailController().getDefaultCurrency(displayCountryCode);
		return ResponseEntity.ok(bulkservice.pushProductToKafkaByUUIDs(request.getProductIds(),displayCountryCode,displayCurrencyCode));
	}

	@GetMapping("/pushSupplierProductsToKafka/{supplierId}")
	public ResponseEntity<?> PushSupplierInventory(@PathVariable ("supplierId") Long supplierId,@RequestParam(value = "displayCountryCode", required = false) String displayCountryCode) {

		_logger.info("Inside PushInventory of pushSupplierProductsToKafka with displayCountryCode:: {} supplierId:: {}",displayCountryCode ,supplierId);
		String displayCurrencyCode=new ProductDetailController().getDefaultCurrency(displayCountryCode);
		return ResponseEntity.ok(bulkservice.pushSupplierProductToKafka(supplierId,displayCountryCode,displayCurrencyCode));
	}
	
	
	
}
