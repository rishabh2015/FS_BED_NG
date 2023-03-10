package com.kb.catalogInventory.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kb.catalogInventory.exception.ControllerExceptionHandler;
import com.kb.catalogInventory.model.LockItemsRQ;
import com.kb.catalogInventory.model.UpdateInventoryRQ;
import com.kb.catalogInventory.service.ProductStockService;

@RestController
@CrossOrigin
@RequestMapping("/productStock")

@Configuration
@PropertySource("classpath:application.properties")
@Validated
public class ProductStockController extends ControllerExceptionHandler{
	
	@Autowired
	ProductStockService productStockService;
	
	@Value("${BOOKINGERRORMSG}")
	private String BOOKINGERRORMSG;
	
	private final Logger _logger = LoggerFactory.getLogger(ProductStockController.class);
	
	@PostMapping("/decrease")
	public ResponseEntity<?> decreaseInventory(
			@Valid @RequestBody @NotNull UpdateInventoryRQ updateInventoryRQ){
		_logger.debug("Entered decreaseInventory with updateInventoryRQ : {}",updateInventoryRQ);
		try {
			productStockService.freezeStock(updateInventoryRQ.getUniqueIdentifier(),updateInventoryRQ.getQuantity());
		} catch (Exception e) {
			_logger.info("Exception in decreaseInventory : {}",e.getMessage());
			return ResponseEntity.ok(e.getMessage());
		}
		_logger.debug("Levaing decreaseInventory with Status: {}",HttpStatus.SC_ACCEPTED);
		return ResponseEntity.ok(HttpStatus.SC_ACCEPTED);
	}
	
	@PostMapping("/decrease/list")
	public ResponseEntity<?> decreaseInventory(@Valid @RequestBody @NotNull List<UpdateInventoryRQ> inventoryList) {
		_logger.debug("Entered decreaseInventory with inventoryList : {}",inventoryList);
		Map<String, String> errorListMap = new HashMap<>();
		for (UpdateInventoryRQ inventory : inventoryList) {
			try {
				productStockService.freezeStock(inventory.getUniqueIdentifier(), inventory.getQuantity());
			} catch (Exception e) {
				_logger.info("Exception in decreaseInventory : {}",e.getMessage());
				errorListMap.put(inventory.getUniqueIdentifier(), e.getMessage());
			}
		}
		if (errorListMap.size() > 0) {
			_logger.info("Exception in decreaseInventory Error List Map: {}",errorListMap);
			return ResponseEntity.ok(errorListMap);
		}
		_logger.debug("Leaving decreaseInventory with Status : "+HttpStatus.SC_ACCEPTED);
		return ResponseEntity.ok(HttpStatus.SC_ACCEPTED);
	}
	
	
	@PostMapping("/increase")
	public ResponseEntity<?> increaseInventory(@Valid @RequestBody @NotNull UpdateInventoryRQ updateInventoryRQ){
		_logger.debug("Entered increaseInventory with updateInventoryRQ : {}",updateInventoryRQ);
		productStockService.updateStock(updateInventoryRQ.getUniqueIdentifier(),updateInventoryRQ.getQuantity());
		_logger.debug("Leaving increaseInventory with Status :"+HttpStatus.SC_ACCEPTED);	
		return ResponseEntity.ok(HttpStatus.SC_ACCEPTED);
	}
	
	@PostMapping("/increase/list")
	public ResponseEntity<?> increaseInventory(@Valid @RequestBody @NotNull List<UpdateInventoryRQ> inventoryList){
		_logger.debug("Entered increaseInventory List with inventoryList : {}",inventoryList);
		for(UpdateInventoryRQ inventory : inventoryList) {
			productStockService.updateStock(inventory.getUniqueIdentifier(),inventory.getQuantity());
		}
		_logger.debug("Leaving increaseInventory list with status :"+HttpStatus.SC_ACCEPTED);
		return ResponseEntity.ok(HttpStatus.SC_ACCEPTED);
	}
	
	@PostMapping("/lockItems")
	public ResponseEntity<?> lockItems(@Valid @RequestBody @NotNull LockItemsRQ lockItemsRQ) {
		_logger.info("Entered lockItems with lockItemsRQ.orderAttemptId : {} and prodQtyMap:{}",lockItemsRQ.getOrderAttemptId(),lockItemsRQ.getLockedItemsToQtyMap().toString());
		try {
			lockItemsRQ = productStockService.saveLockItems(lockItemsRQ);	
		}catch (Exception e) {
			_logger.error("Exception in lockItems : ",e);
			return ResponseEntity.ok(e.getMessage());
		}
		_logger.info("Leaving lockItems with  lockItemsRQ: {}",lockItemsRQ);
		return ResponseEntity.ok(lockItemsRQ);
	}
	
	@GetMapping("/updateOrderAttempt/{intialOrderAttempt}/{orderAttemptWithPaymentAttempt}/{unlock}")
	public ResponseEntity<?> updateOrderAttempt(@PathVariable("intialOrderAttempt")String intialOrderAttempt, @PathVariable("orderAttemptWithPaymentAttempt")String orderAttemptWithPaymentAttempt, @PathVariable("unlock")Boolean unlock) {
		_logger.debug("Entered updateOrderAttempt with intialOrderAttempt and orderAttemptWithPaymentAttempt and unlock : {} :{} :{}",intialOrderAttempt,orderAttemptWithPaymentAttempt,unlock);
		LockItemsRQ lockItemsRQ=null;
		try {
			lockItemsRQ = productStockService.updateOrderAttempt(intialOrderAttempt, orderAttemptWithPaymentAttempt, unlock);
		}catch (Exception e) {
			_logger.error("Exception in updateOrderAttempt : ",e);
			return ResponseEntity.ok(e.getMessage());
		}
		_logger.debug("Leaving updateOrderAttempt with  lockItemsRQ: {}",lockItemsRQ);
		return ResponseEntity.ok(lockItemsRQ);
	}
	
	
	@PostMapping("/updateBooking")
	public ResponseEntity<?> updateBooking(@Valid @RequestBody @NotNull LockItemsRQ lockItemsRQ){
		_logger.debug("Entered updateBooking with lockItemsRQ : {}",lockItemsRQ);
		try {
			lockItemsRQ = productStockService.updateBookingInfo(lockItemsRQ);	
		}catch (Exception e) {
			_logger.info("Exception in updateBooking : {}",e);
			HashMap<String, Object> obj = new HashMap<>();
			List<String> msgList = new ArrayList<>();
			msgList.add(BOOKINGERRORMSG);
			obj.put("status", "FAILED");
		    obj.put("message", msgList);
			return ResponseEntity.ok(obj);
		}
		_logger.debug("Leaving updateBooking with lockItemsRQ : {}",lockItemsRQ.toString());
		return ResponseEntity.ok(lockItemsRQ);
	}
	
	
	@PostMapping("offlineOrder/decrease/list")
	public ResponseEntity<?> decreaseInventoryForOfflineOrder(@Valid @RequestBody @NotNull Map<String,List<UpdateInventoryRQ>> inventoryListMap) {
		List<UpdateInventoryRQ> inventoryList = inventoryListMap.get("offlineOrderDecreaseInventoryMap");
		_logger.info("Entered decreaseInventory with inventoryList : {}",inventoryList);
		Map<String, String> responseMap = new HashMap<String, String>();
		for (UpdateInventoryRQ inventory : inventoryList) {
			try {
				productStockService.freezeStockOfflineOrder(inventory.getUniqueIdentifier(), inventory.getQuantity());
			} catch (Exception e) {
				_logger.info("Exception in decreaseInventory : {}",e.getMessage());
				responseMap.put(inventory.getUniqueIdentifier(), e.getMessage());
			}
		}
		if (responseMap.size() > 0) {
			_logger.info("Exception in decreaseInventory Error List Map: {}",responseMap);
			return ResponseEntity.ok(responseMap);
		}else {
			responseMap.put("Status", Integer.toString(HttpStatus.SC_ACCEPTED));
		}
		_logger.debug("Leaving decreaseInventory with Status : "+HttpStatus.SC_ACCEPTED);
		return ResponseEntity.ok(responseMap);
	}
	

	
}
