package com.kb.catalogInventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kb.catalogInventory.constant.InventoryConstants;
import com.kb.catalogInventory.datatable.LockedItems;
import com.kb.catalogInventory.datatable.ProductCombinations;
import com.kb.catalogInventory.datatable.ProductStock;
import com.kb.catalogInventory.exception.InventoryException;
import com.kb.catalogInventory.model.BestSellingRQ;
import com.kb.catalogInventory.model.LockItemsRQ;
import com.kb.catalogInventory.model.ProductCacheBO;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.model.RemoveProductBO;
import com.kb.catalogInventory.repository.LockItemRepository;
import com.kb.catalogInventory.repository.ProductCombinationsRepository;
import com.kb.catalogInventory.repository.ProductStockRepository;
import com.kb.catalogInventory.scheduler.UnLockInventoryStockScheduler;
import com.kb.java.utils.KbRestTemplate;
import com.kb.kafka.producer.KafkaBroadcastingService;
import com.kb.kafka.producer.KafkaBrodcastingRequest;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
public class ProductStockService {

	@Autowired
	private ProductCombinationsRepository productCombinationsRepository;

	@Autowired
	private LockItemRepository lockItemRepository;

	@Autowired
	private UnLockInventoryStockScheduler unLockInventoryStock;


	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private ProductCacheService productCache;
	
	@Autowired
	private ProductUploadService productUploadService;

	@Autowired
	private ProductStockRepository productStockRepository;


	@Value("${order.service.bestselling.getsellingcountdetail.url}")
	private String getSellingCountDetailsUrl;

	@Value("${order.service.bestselling.getsellingcountdetail.url.timeout}")
	private int getSellingCountDetailsUrlTimeout;

	@Autowired
	KbRestTemplate restTemplate;

	private final Logger _logger = LoggerFactory.getLogger(ProductStockService.class);

	private static final ObjectMapper _mapper = new ObjectMapper();

	public void freezeStock(String uniqueId,String quantity) throws Exception {

		_logger.info("Inside freezeStock uniqueId:: {}",uniqueId);
		ProductCombinations pc = productCombinationsRepository.findByStockUniqueIdentifier(uniqueId);
		if(pc.getAvailableStock() >=Integer.parseInt(quantity)) {
			pc.setAvailableStock(pc.getAvailableStock() - Integer.parseInt(quantity));
			pc.setUpdatedOn(new Date(System.currentTimeMillis()));
			productCombinationsRepository.save(pc);
		}else {
			throw new InventoryException(InventoryConstants.QUANTITY_NOT_AVAILABLE);
		}

	}


	public void updateStock(String uniqueId,String quantity) {
		_logger.info("Inside updateStock uniqueId:: {}",uniqueId);
		ProductCombinations pc = productCombinationsRepository.findByStockUniqueIdentifier(uniqueId);
		pc.setAvailableStock(pc.getAvailableStock() + Integer.parseInt(quantity));
		pc.setUpdatedOn(new Date(System.currentTimeMillis()));
		productCombinationsRepository.save(pc);
	}

	public LockItemsRQ saveLockItems(LockItemsRQ lockItemsRQ){
		_logger.debug("Inside saveLockItems uniqueId:: {}",new Gson().toJson(lockItemsRQ));
		_logger.info("Inside saveLockItem for lockItem orderAttempt "+lockItemsRQ.getOrderAttemptId());
		try {


			LockedItems li = new LockedItems();
			List<String> msgList = new ArrayList<>();
			String notInStockProducts ="";
			String json ="";
			HashMap<String,Integer> map = lockItemsRQ.getLockedItemsToQtyMap();
			boolean isTotalStockAvailable = true;
			try {
				long t1 = System.currentTimeMillis();
				List<LockedItems> lockedIteams=lockItemRepository.findByCartIdAndBookingCreatedFalse(lockItemsRQ.getCartId());
				long t2 = System.currentTimeMillis();
				_logger.error("in Unlock inventory step1 "+ (t2-t1));
//				if(null!=lockedIteams) {
//					lockedIteams.stream().forEach(li1->_logger.info("going to unlock lockedItems of orderAttemptId "+li1.getOrderAttemptId()+" locked at "+li1.getLastUpdateTime()));
//					for(LockedItems lockedItems : lockedIteams) {
//						if(lockedItems.isActive() && !lockedItems.isBookingCreated()) {
//							unLockInventoryStock.unlockedLockedIteam(lockedItems,"-from lockitems");
//						}
//
//					}
//
//				}
				long t3 = System.currentTimeMillis();
				_logger.error("in Unlock inventory step2 "+ (t3-t2));
				

				if(map.size()>0) {
					for (Map.Entry<String,Integer> entry : map.entrySet()) {
						// change the query to select by available stock
						//  Integer availableStock=productCombinationsRepository.findStockOfProduct(entry.getKey());
						ProductCombinations pc = productCombinationsRepository.findByStockByUniqueIdentifier
								(entry.getKey());
						_logger.info("stock Checking  for product "+entry.getKey()+ "for requested quantity "
						     	+entry.getValue()+  " AvailableStock() "+pc.getAvailableStock()  );
								
						if(pc==null || pc.getAvailableStock()==0 || pc.getAvailableStock() <entry.getValue()) {
							_logger.info("stock not available  for product "+entry.getKey()+ "for requested quantity "
					     	+entry.getValue()+  " AvailableStock() "+pc.getAvailableStock()  +" inside finding isTotalStockAvailable");
							isTotalStockAvailable = false;
							notInStockProducts = entry.getKey();
							break;
						}
					}

				}
				long t4 = System.currentTimeMillis();
				_logger.error("in Unlock inventory step3 "+ (t4-t3));
				
				List<ProductCombinations> pcToRemoveFrmElastic=new ArrayList<>();
				List<String> pcToUpdateElastic= new ArrayList<>();
				_logger.info("isTotalStockAvailable "+isTotalStockAvailable +" for orderAttemptId "+lockItemsRQ.getOrderAttemptId());
				if(isTotalStockAvailable){
					_logger.info("Stocks for all products available for orderAttemptId "+lockItemsRQ.getOrderAttemptId() +" and cartId "+lockItemsRQ.getCartId());
					boolean isUpdated = updateStock(isTotalStockAvailable, lockItemsRQ, map, pcToRemoveFrmElastic, pcToUpdateElastic);
					if (!isUpdated) {
						_logger.info("Failed to update stock  for orderAttemptId "+lockItemsRQ.getOrderAttemptId() +" and cartId "+lockItemsRQ.getCartId());
						lockItemsRQ.setBookingCreated(false);
						lockItemsRQ.setBookingId(null);
						lockItemsRQ.setCartId(null);
						lockItemsRQ.setLastUpdateTime(null);
						lockItemsRQ.setLockedItemsToQtyMap(null);
						lockItemsRQ.setLockedtime(null);
						msgList.add("Failed to update stocks for orderAttemptId "+lockItemsRQ.getOrderAttemptId() +" and cartId "+lockItemsRQ.getCartId());
						lockItemsRQ.setMesssage(msgList);
						lockItemsRQ.setOrderAttemptId(null);
						lockItemsRQ.setStatus("ERROR");
						lockItemsRQ.setTotalPrice(0);
						lockItemsRQ.setUserId(0);
						return lockItemsRQ;
					}
					long t5 = System.currentTimeMillis();
					_logger.error("in Unlock inventory step4 "+ (t5-t4));
					
					int cores = Runtime.getRuntime().availableProcessors();
					_logger.info("Available core on node --- {}",cores);
					if(pcToRemoveFrmElastic!=null && pcToRemoveFrmElastic.size()>0) {
						pcToRemoveFrmElastic.stream().forEach(pc->{
							try {
								RemoveProductBO pi = RemoveProductBO.builder().availableStock(pc.getAvailableStock())
										.productCombinationId(pc.getUniqueIdentifier()).removeProductFromList(true).build();
								sendToKafka(pi);
							}catch (Exception e) {
								_logger.error("Error in parallel stream in lockItem purging -- ",e);
							}

						});

					}
					long t6 = System.currentTimeMillis();
					_logger.error("in Unlock inventory step5 "+ (t6-t5));
					
					// productMapper.createProductWrapper(pcToUpdateElastic, "IN","INR");
					//Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(pcToUpdateElastic);
					Map<String, Long> bestSellingProductCountDetailMap = new HashMap<>();
					pcToUpdateElastic.stream().forEach(uuid -> {
						try {
//							ProductInventoryBO bo = productUploadService.populateProductInventoryObj(uuid,
//									"IN", "INR", bestSellingProductCountDetailMap,null, "0",false);
//							sendToKafka(bo);
							
							ProductCacheBO 	bo = productCache.getProductFromCache(uuid, "IN", "INR");
							sendToKafkaCacheBO(bo);
						} catch (Exception e) {
							_logger.error("Error while updating product to kafka while locking item ", e);
						}
					});
					
					ObjectMapper objectMapper = new ObjectMapper();

					try {
						json = objectMapper.writeValueAsString(map);
						_logger.debug(json);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					_logger.info("Going to successfully Save lock for order attempt "+lockItemsRQ.getOrderAttemptId() );
					li.setCartId(lockItemsRQ.getCartId());
					li.setOrderAttemptId(lockItemsRQ.getOrderAttemptId());
					li.setTotalPrice(lockItemsRQ.getTotalPrice());
					li.setUserId(lockItemsRQ.getUserId());
					li.setStatus("LOCKED");
					li.setLastUpdateTime(new Date(System.currentTimeMillis()));
					li.setLockedtime(new Date(System.currentTimeMillis()));
					li.setLockedItemsToQtyMap(json);
					li.setActive(true);
					lockItemRepository.save(li);

					lockItemsRQ.setStatus("LOCKED");
					lockItemsRQ.setLastUpdateTime(li.getLastUpdateTime());
					lockItemsRQ.setLockedtime(li.getLockedtime());
					lockItemsRQ.setLockedItemsToQtyMap(map);
					lockItemsRQ.setBookingCreated(li.isBookingCreated());
					lockItemsRQ.setBookingId(li.getBookingId());
					long t7 = System.currentTimeMillis();
					_logger.error("in Unlock inventory step6 "+ (t7-t6));
					
				}else {
					_logger.info("Selected quantity is more than available stock for any of the product of orederAttemptId"+lockItemsRQ.getOrderAttemptId());
					lockItemsRQ.setBookingCreated(false);
					lockItemsRQ.setBookingId(null);
					lockItemsRQ.setCartId(null);
					lockItemsRQ.setLastUpdateTime(null);
					lockItemsRQ.setLockedItemsToQtyMap(null);
					lockItemsRQ.setLockedtime(null);
					msgList.add("Quantity Is more than Available Product for Product Id: "+notInStockProducts);
					lockItemsRQ.setMesssage(msgList);
					lockItemsRQ.setOrderAttemptId(null);
					lockItemsRQ.setStatus("ERROR");
					lockItemsRQ.setTotalPrice(0);
					lockItemsRQ.setUserId(0);
					throw new Exception("Quantity Is more than Available Product");


				}
			}catch(Exception e) {
				_logger.error("Exception inside savelock item 1 for orderAttemptId :"+lockItemsRQ.getOrderAttemptId(),e);
				throw new InventoryException(InventoryConstants.ITEM_LOCK_EXCEPTION);

			}

		}catch (Exception e) {
			_logger.error("ERROR While locking the items 2 for orderAttemptId : "+lockItemsRQ.getOrderAttemptId(),e);
			_logger.error("ERROR While locking the items for RQ"+new Gson().toJson(lockItemsRQ),e);
			return lockItemsRQ;
		}
		return lockItemsRQ;
	}

	public Map<String, Long> fetchBestSellingProductDetailCount(List<String> productIdList) {
		Map<String, Long> outputMap = new HashMap<>();
		try {
			//			HttpHeaders headers = new HttpHeaders();
			//			headers.setContentType(MediaType.APPLICATION_JSON);
			//			HttpEntity<BestSellingRQ> entity = new HttpEntity<>(BestSellingRQ.builder().productIds(productIdList).build(), headers);
			//			ResponseEntity<String> result = restTemplate.exchange(
			//					getSellingCountDetailsUrl, HttpMethod.POST, entity, getSellingCountDetailsUrlTimeout, String.class);
			//			if (result.getStatusCode() == HttpStatus.OK) {
			//				JSONObject response = new JSONObject(Objects.requireNonNull(result.getBody()));
			//				JSONArray jsonArray = response.optJSONArray("data");
			//				if (jsonArray != null && jsonArray.length() > 0) {
			//					for (int index = 0; index < jsonArray.length(); index++) {
			//						JSONObject object = jsonArray.optJSONObject(index);
			//						outputMap.put(object.optString("productId"), object.optLong("sellingCount"));
			//					}
			//				}
			//			}
			productIdList.forEach(productId -> outputMap.put(productId, 0L));
		} catch (Exception e) {
			_logger.error("Exception occurred while data parsing of bestSellingProductDetails", e);
		}
		return outputMap;
	}

	public LockItemsRQ updateBookingInfo(LockItemsRQ lockItemsRQ) throws Exception {
		_logger.info("updateBookingInfo : {}",new Gson().toJson(lockItemsRQ) );
		LockedItems li = new LockedItems();
		List<String> msgList = new ArrayList<>();

		//lockItemRepository.refrainConcurrentModification(lockItemsRQ.getOrderAttemptId());
		LockedItems lockedItem = lockItemRepository.checkIsActive(lockItemsRQ.getOrderAttemptId());

		_logger.info("Booking ID For Unlock "+lockItemsRQ.getBookingId() +" isactive "+lockedItem.isActive());

		if(lockedItem.isActive()) {
			Date lockUpdateTime = new Date(System.currentTimeMillis());
			lockItemRepository.updateBookingInfo(lockItemsRQ.getBookingId(), lockUpdateTime, lockedItem.getId());
			li = lockedItem;
			msgList.add("Booking Created Successfully");				

			String lockedItemQtyMap = li.getLockedItemsToQtyMap();
			HashMap<String,Integer> map = new HashMap<>();
			lockedItemQtyMap = lockedItemQtyMap.replace("{", "");
			lockedItemQtyMap = lockedItemQtyMap.replace("}", "");
			String[] a1 = lockedItemQtyMap.split(",");
			for(String s:a1) {
				String[] s1 = s.split(":");
				map.put(s1[0].replace("\"", ""), Integer.parseInt(s1[1]));
			}

			lockItemsRQ.setUserId(li.getUserId());
			lockItemsRQ.setCartId(li.getCartId());
			lockItemsRQ.setOrderAttemptId(li.getOrderAttemptId());
			lockItemsRQ.setLockedItemsToQtyMap(map);
			lockItemsRQ.setTotalPrice(li.getTotalPrice());
			lockItemsRQ.setLockedtime(li.getLockedtime());

			lockItemsRQ.setStatus("PROCESSED");
			lockItemsRQ.setBookingCreated(true);
			lockItemsRQ.setBookingId(lockItemsRQ.getBookingId());
			lockItemsRQ.setLastUpdateTime(lockUpdateTime);
			lockItemsRQ.setMesssage(msgList);

		}else {
			_logger.error("No active Loceked Items for order attempt "+lockItemsRQ.getOrderAttemptId());

			throw new Exception("Exception in Locking the Items");			
		}	

		return lockItemsRQ;

	}

	@Autowired
	private KafkaBroadcastingService kafkaBroadcastingService;

	public void sendToKafka(RemoveProductBO pi){
		sendToKafka(pi,false);
	}
	public void sendToKafka(RemoveProductBO pi,boolean scheduledProcessing) {

		_logger.debug("Inside sendToKafka :: {}",new Gson().toJson(pi));
		try {
			String productInventryString = _mapper.writeValueAsString(pi);
			String topicName = "kb-catalog-data-purge-topic";
			if(scheduledProcessing){
				topicName = "kb-catalog-data-purge-from-scheduler-topic";
			}
			KafkaBrodcastingRequest kbr = new KafkaBrodcastingRequest(topicName,
					pi.getProductCombinationId(), productInventryString);
			kafkaBroadcastingService.broadcast(kbr);
		} catch (JsonProcessingException e) {
			_logger.error("Remove Product not send to kafka ............",e);
		} catch (Exception e) {
			_logger.error("Remove Product not send to kafka ............",e);
		}

	}

	public void sendToKafka(ProductInventoryBO pi) {
		ObjectMapper _mapper = new ObjectMapper();
		try {
			if(StringUtils.isNotBlank(pi.getUniqueIdentifierForSearch())) {
				_logger.info("pushing  pc  to kafka with id ............{}",pi.getUniqueIdentifierForSearch());
				String productInventryString = _mapper.writeValueAsString(pi);
				KafkaBrodcastingRequest kbr = new KafkaBrodcastingRequest("kb-catalog-data-topic",
						pi.getUniqueIdentifierForSearch(), productInventryString);
				kafkaBroadcastingService.broadcast(kbr);
			}
		} catch (JsonProcessingException e) {
			_logger.error("JsonProcessingException product inventory not send to kafka ............",e);
		} catch (Exception e) {
			_logger.error("Exception product inventory not send to kafka ............",e);
		} 

	}
	public void sendToKafkaCacheBO(ProductCacheBO pi) {
		ObjectMapper _mapper = new ObjectMapper();
		try {
			if(StringUtils.isNotBlank(pi.getUniqueIdentifierForSearch())) {
				_logger.info("pushing  pc  to sendToKafkaCacheBO with id ............{}",pi.getUniqueIdentifierForSearch());
				String productInventryString = _mapper.writeValueAsString(pi);
				KafkaBrodcastingRequest kbr = new KafkaBrodcastingRequest("kb-catalog-data-topic",
						pi.getUniqueIdentifierForSearch(), productInventryString);
				kafkaBroadcastingService.broadcast(kbr);
			}
		} catch (JsonProcessingException e) {
			_logger.error("JsonProcessingException  sendToKafkaCacheBO product inventory not send to kafka ............",e);
		} catch (Exception e) {
			_logger.error("Exception product isendToKafkaCacheBO nventory not send to kafka ............",e);
		} 

	}

	
	

	public ProductCombinations freezeStockOfflineOrder(String uniqueId,String quantity) throws Exception {
		_logger.info("Inside freezeStockOfflineOrder :: {}",uniqueId);


		ProductCombinations pc = productCombinationsRepository.findByStockUniqueIdentifier(uniqueId);
		if(pc.getAvailableStock() >=Integer.parseInt(quantity)) {
			pc.setAvailableStock(pc.getAvailableStock() - Integer.parseInt(quantity));
			pc.setUpdatedOn(new Date(System.currentTimeMillis()));
		}else {
			pc.setAvailableStock(0);
			pc.setUpdatedOn(new Date(System.currentTimeMillis()));
		}
		productCombinationsRepository.save(pc);
		return pc;
	}


	public LockItemsRQ updateOrderAttempt(String intialOrderAttempt, String orderAttemptWithPaymentAttempt, Boolean unlock) {
		LockItemsRQ lockItemsRQ= new LockItemsRQ();
		lockItemRepository.refrainConcurrentModification(intialOrderAttempt);
		LockedItems li= lockItemRepository.findbyorderAttemptId(intialOrderAttempt);
		if(null!=li &&li.isActive() ) {
			li.setOrderAttemptId(orderAttemptWithPaymentAttempt);
			li= lockItemRepository.save(li);

			if(unlock && !li.isBookingCreated()) {
				unLockInventoryStock.unlockedLockedIteam(li,"-from updateOrderAttempt");
			}


			String lockedItemQtyMap = li.getLockedItemsToQtyMap();
			HashMap<String,Integer> map = new HashMap<>();
			lockedItemQtyMap = lockedItemQtyMap.replace("{", "");
			lockedItemQtyMap = lockedItemQtyMap.replace("}", "");
			String[] a1 = lockedItemQtyMap.split(",");
			for(String s:a1) {
				String[] s1 = s.split(":");
				map.put(s1[0].replace("\"", ""), Integer.parseInt(s1[1]));
			}

			lockItemsRQ.setUserId(li.getUserId());
			lockItemsRQ.setCartId(li.getCartId());
			lockItemsRQ.setOrderAttemptId(li.getOrderAttemptId());
			lockItemsRQ.setTotalPrice(li.getTotalPrice());
			lockItemsRQ.setLockedtime(li.getLockedtime());
			lockItemsRQ.setStatus("PROCESSED");
			lockItemsRQ.setBookingCreated(true);
			lockItemsRQ.setBookingId(lockItemsRQ.getBookingId());
			lockItemsRQ.setLastUpdateTime(new Date(System.currentTimeMillis()));
			List<String> msgList=new ArrayList<>();
			msgList.add("OrderAttempt replaced with orderAttemptWithPaymentAttempt");
			lockItemsRQ.setMesssage(msgList);
		}else {
			String lockedItemQtyMap = li.getLockedItemsToQtyMap();
			HashMap<String,Integer> map = new HashMap<>();
			lockedItemQtyMap = lockedItemQtyMap.replace("{", "");
			lockedItemQtyMap = lockedItemQtyMap.replace("}", "");
			String[] a1 = lockedItemQtyMap.split(",");
			for(String s:a1) {
				String[] s1 = s.split(":");
				map.put(s1[0].replace("\"", ""), Integer.parseInt(s1[1]));
			}

			lockItemsRQ.setUserId(li.getUserId());
			lockItemsRQ.setCartId(li.getCartId());
			lockItemsRQ.setOrderAttemptId(li.getOrderAttemptId());
			lockItemsRQ.setTotalPrice(li.getTotalPrice());
			lockItemsRQ.setLockedtime(li.getLockedtime());
			lockItemsRQ.setStatus("INACTIVE");
			lockItemsRQ.setBookingCreated(true);
			lockItemsRQ.setBookingId(lockItemsRQ.getBookingId());
			lockItemsRQ.setLastUpdateTime(new Date(System.currentTimeMillis()));
			List<String> msgList=new ArrayList<>();
			msgList.add("OrderAttempt couldNot be replaced with orderAttemptWithPaymentAttempt");
			lockItemsRQ.setMesssage(msgList);
		}

		return lockItemsRQ;

	}
	@Transactional
	private boolean updateStock(boolean isTotalStockAvailable, LockItemsRQ lockItemsRQ, Map<String, Integer> productToQuantityMap, List<ProductCombinations> pcToRemoveFrmElastic, List<String> pcToUpdateElastic) {
		boolean isUpdated = false;
		if (isTotalStockAvailable) {
			_logger.info("Stock is available for all products of OrderAttemptId" + lockItemsRQ.getOrderAttemptId()+" and cartId "+lockItemsRQ.getCartId());
			for (Map.Entry<String, Integer> entry : productToQuantityMap.entrySet()) {
				int updated=0;
				isUpdated = false;
				Map<String,Object> resMap=null;
				for (int i=0;i<3;i++) {
					resMap= selectAndUpdateLock(entry.getKey(), entry.getValue());
					updated=(Integer) resMap.get("updated");
					if(updated>0){
						break;
					}else{
						_logger.info("Stock Lock FAILED for UUID "+entry.getKey() +" and requested Quantity "+entry.getValue() + " for "+i+" times");
					
					}
				}

				_logger.info("isUpdated for UUID "+entry.getKey()+" for requested quantity "+entry.getValue()+ " is "+updated);
				if(updated>0) {
					ProductCombinations pc =(ProductCombinations) resMap.get("pc");
					isUpdated = true;
					if (pc.getAvailableStock() == 0) {
						_logger.info("Stock emptied  for product "+entry.getKey()+" after requested quantity "+entry.getValue() );
						pcToRemoveFrmElastic.add(pc);
					} else {
						pcToUpdateElastic.add(pc.getUniqueIdentifier());
					}

					ProductStock productStock = new ProductStock();
					productStock.setInsertedOn(new Date());
					productStock.setIsActive(true);
					productStock.setProductCombinations(pc);
					productStock.setUnitPrice(pc.getPrice());
					productStock.setTotalStock(pc.getAvailableStock());
					productStock.setTotalPrice(productStock.getUnitPrice() * productStock.getTotalStock());
					productStock.setOldStock(pc.getAvailableStock() + entry.getValue());
					productStock.setStockChangeComment("Stock changed by Lockitems from cart " + lockItemsRQ.getCartId() + ", Old Stock was " + productStock.getOldStock() + " and new Stock is " + productStock.getTotalStock());
					productStock.setUpdatedOn(new Date());
					productStockRepository.save(productStock);
				}else{
					_logger.info("Stock not updated for product "+entry.getKey()+" and requested quantity "+entry.getValue() +" as updated variable is "+updated);

				}
			}
		}
		return isUpdated;
	}
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	private Map<String,Object> selectAndUpdateLock(String UUID,Integer requestedQuantity){
		Map<String,Object> updateLockMap=new HashMap<>();
		_logger.info("Inside selectAndUpdateLock For UUID "+UUID +" and requested quantity "+requestedQuantity);
		ProductCombinations pc = productCombinationsRepository.findByStockUniqueIdentifier(UUID);
		_logger.info("available stock of product " + pc.getUniqueIdentifier() +  " is " + pc.getAvailableStock());

		Integer reducedStock = pc.getAvailableStock() - requestedQuantity;


		_logger.info("Going to update STOCK of product " + pc.getUniqueIdentifier() +  " from available stock " + pc.getAvailableStock() +" to STOCK "+reducedStock);
		int updated=	productCombinationsRepository.updateStockOfProduct(UUID, reducedStock);
		_logger.info("Leaving selectAndUpdateLock For UUID "+UUID +" and requested quantity "+requestedQuantity +" with updated rows "+updated);

		pc.setAvailableStock(reducedStock);
		updateLockMap.put("pc",pc);
		updateLockMap.put("updated",updated);
		return updateLockMap;
	}
	
	@Transactional(Transactional.TxType.REQUIRES_NEW)
	public int selectAndUpdateStock(String UUID,Integer requestedQuantity){
		_logger.info("Inside selectAndUpdateLock For UUID "+UUID +" and requested quantity "+requestedQuantity);
		ProductCombinations pc = productCombinationsRepository.findByStockUniqueIdentifier(UUID);
		_logger.info("available stock of product " + pc.getUniqueIdentifier() +  " is " + pc.getAvailableStock());

		Integer reducedStock = pc.getAvailableStock() + requestedQuantity;


		_logger.info("Going to update STOCK of product " + pc.getUniqueIdentifier() +  " from available stock " + pc.getAvailableStock() +" to STOCK "+reducedStock);
		int updated=	productCombinationsRepository.updateStockOfProduct(UUID, reducedStock);
		_logger.info("Leaving selectAndUpdateLock For UUID "+UUID +" and requested quantity "+requestedQuantity +" with updated rows "+updated);

		
		return updated;
	}
}
