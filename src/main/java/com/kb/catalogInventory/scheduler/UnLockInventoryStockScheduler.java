package com.kb.catalogInventory.scheduler;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.kb.catalogInventory.model.BestSellingRQ;
import com.kb.java.utils.KbRestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kb.catalogInventory.datatable.LockedItems;
import com.kb.catalogInventory.datatable.ProductCombinations;
import com.kb.catalogInventory.datatable.ProductStatus;
import com.kb.catalogInventory.datatable.ProductStock;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.model.StatusConstant;
import com.kb.catalogInventory.repository.LockItemRepository;
import com.kb.catalogInventory.repository.ProductCombinationsRepository;
import com.kb.catalogInventory.repository.ProductStatusRepository;
import com.kb.catalogInventory.repository.ProductStockRepository;
import com.kb.catalogInventory.service.ProductUploadService;
import com.kb.kafka.producer.KafkaBroadcastingService;
import com.kb.kafka.producer.KafkaBrodcastingRequest;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class UnLockInventoryStockScheduler {

	@Autowired
	private LockItemRepository lockRepository;
	
	@Autowired
	private ProductCombinationsRepository inventoryRepository;
	
	private  ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private ProductUploadService productUploadService;
	
	@Autowired
	KafkaBroadcastingService kafkaBroadcastingService;
	
	@Autowired
	private ProductStatusRepository productStatusRepository;

	@Autowired
	private ProductStockRepository productStockRepository;

	@Value("${order.service.bestselling.getsellingcountdetail.url}")
	private String getSellingCountDetailsUrl;

	@Value("${order.service.bestselling.getsellingcountdetail.url.timeout}")
	private int getSellingCountDetailsUrlTimeout;

	@Autowired
	KbRestTemplate restTemplate;

	@Value("${run.scheduler}")
	private boolean runScheduler;


	private final static Logger _logger = LoggerFactory.getLogger(UnLockInventoryStockScheduler.class);
	
	@Scheduled(fixedDelay = 1*60*1000l)
	public void unlockStockInInventory() {

		if(!runScheduler){
			_logger.warn("Not running unlock stock in inventory scheduler");
			return;
		}
		
		lockRepository.findByisActive(true).stream().forEach(lockedItem -> {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long timediff = 0l;
			try {
				timediff = formatter.parse(formatter.format(new Date(System.currentTimeMillis()))).getTime()
						- lockedItem.getLastUpdateTime().getTime();
			} catch (ParseException e1) {
				_logger.error(" ",e1);
			}
			long timediffInMinute = (timediff / (1000 * 60)) % 60;
			if (timediffInMinute > 15) {
				_logger.info("**********Going to unlock item with indentifier : "+lockedItem.getId());
				unlockedLockedIteam(lockedItem,"- from unlock scheduler");
			}
		});
		
	}

	public Map<String, Long> fetchBestSellingProductDetailCount(List<String> productIdList) {
		Map<String, Long> outputMap = new HashMap<>();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<BestSellingRQ> entity = new HttpEntity<>(BestSellingRQ.builder().productIds(productIdList).build(), headers);
			ResponseEntity<String> result = restTemplate.exchange(
					getSellingCountDetailsUrl, HttpMethod.POST, entity, getSellingCountDetailsUrlTimeout, String.class);
			if (result.getStatusCode() == HttpStatus.OK) {
				JSONObject response = new JSONObject(Objects.requireNonNull(result.getBody()));
				JSONArray jsonArray = response.optJSONArray("data");
				if (jsonArray != null && jsonArray.length() > 0) {
					for (int index = 0; index < jsonArray.length(); index++) {
						JSONObject object = jsonArray.optJSONObject(index);
						outputMap.put(object.optString("productId"), object.optLong("sellingCount"));
					}
				}
			}
		} catch (Exception e) {
			_logger.error("Exception occurred while data parsing of bestSellingProductDetails", e);
		}
		return outputMap;
	}



	public void unlockedLockedIteam(LockedItems lockedItem,String remark) {
		log.debug("Entered unlockedLockedIteam with Input Request {}",new Gson().toJson(lockedItem));
		log.info("Inside Unlock scheduler to unlock orderAttempt  "+lockedItem.getOrderAttemptId());
		TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
		};
		Optional<ProductStatus> productStatus = productStatusRepository.findById(StatusConstant.Deleted_Product);
		List<ProductStatus> psList = new ArrayList();
		psList.add(productStatus.get());
		try {
			if(StringUtils.isNotBlank(lockedItem.getLockedItemsToQtyMap())) {
				_logger.warn("Inside Unlock scheduler to unlock orderAttempt  "+lockedItem.getOrderAttemptId()+ "with product map "+lockedItem.getLockedItemsToQtyMap().toString());
			Map<String, String> map = objectMapper.readValue(lockedItem.getLockedItemsToQtyMap(), typeRef);
			map.keySet().stream().forEach(k->{
				String v = map.get(k);

				try {
					long t1 = System.currentTimeMillis();
					
					_logger.error("Going for Product Identifier->"+k);
				ProductCombinations inventory = inventoryRepository.findByUniqueIdentifierAndProductStatusNotIn(k,psList);
				inventory.setAvailableStock(inventory.getAvailableStock() + Integer.parseInt(v));
				inventoryRepository.save(inventory);
				long t2 = System.currentTimeMillis();
				_logger.error("in Unlock inventory inner method step1 "+ (t2-t1));
				
				//Map<String, Long> bestSellingProductCountDetailMap =
					//	fetchBestSellingProductDetailCount(Collections.singletonList(inventory.getUniqueIdentifier()));
				Map<String, Long> bestSellingProductCountDetailMap = new HashMap<>();
				long t3 = System.currentTimeMillis();
				_logger.error("in Unlock inventory inner method step2 "+ (t3-t2));
			

				ProductInventoryBO pi=productUploadService.populateProductInventoryObj(inventory.getUniqueIdentifier(),
						"IN","INR", bestSellingProductCountDetailMap,null, "0",false);
				sendToKafka(pi,lockedItem);
				lockedItem.setActive(false);
				lockedItem.setStatus("UNLOCKED");
				lockedItem.setLastUpdateTime(new java.sql.Date(System.currentTimeMillis()));
				long t4 = System.currentTimeMillis();
				_logger.error("in Unlock inventory inner method step2 "+ (t4-t3));
			
				lockRepository.save(lockedItem);
				long t5 = System.currentTimeMillis();
				_logger.error("in Unlock inventory inner method step3 "+ (t5-t4));
			
				ProductStock productStock = new ProductStock();
				productStock.setInsertedOn(new Date());
				productStock.setIsActive(true);
				productStock.setProductCombinations(inventory);
				productStock.setUnitPrice(inventory.getPrice());
				productStock.setTotalStock(inventory.getAvailableStock());
				productStock.setTotalPrice(productStock.getUnitPrice()*productStock.getTotalStock());
				productStock.setOldStock(inventory.getAvailableStock() - Integer.parseInt(v));
				productStock.setStockChangeComment("Stock changed by unLockitems from cart "+lockedItem.getCartId()+", Old Stock was "+productStock.getOldStock()+" and new Stock is "+productStock.getTotalStock()+" "+remark);
				productStock.setUpdatedOn(new Date());
				productStockRepository.save(productStock);
				long t6 = System.currentTimeMillis();
				_logger.error("in Unlock inventory inner method step4 "+ (t6-t5));
			
				}catch (Exception e) {
					log.error("Exception while unlockedLockedIteam UNLOCKING the product for orderAttempt id "+lockedItem.getOrderAttemptId(),e);
				}
			
			});
			
			/*map.forEach((k, v) -> {
				try {
					long t1 = System.currentTimeMillis();
					
					_logger.error("Going for Product Identifier->"+k);
				ProductCombinations inventory = inventoryRepository.findByUniqueIdentifierAndProductStatusNotIn(k,psList);
				inventory.setAvailableStock(inventory.getAvailableStock() + Integer.parseInt(v));
				inventoryRepository.save(inventory);
				long t2 = System.currentTimeMillis();
				_logger.error("in Unlock inventory inner method step1 "+ (t2-t1));
				
				//Map<String, Long> bestSellingProductCountDetailMap =
					//	fetchBestSellingProductDetailCount(Collections.singletonList(inventory.getUniqueIdentifier()));
				Map<String, Long> bestSellingProductCountDetailMap = new HashMap<>();
				long t3 = System.currentTimeMillis();
				_logger.error("in Unlock inventory inner method step2 "+ (t3-t2));
			

				ProductInventoryBO pi=productUploadService.populateProductInventoryObj(inventory.getUniqueIdentifier(),
						"IN","INR", bestSellingProductCountDetailMap,null, "0",false);
				sendToKafka(pi,lockedItem);
				lockedItem.setActive(false);
				lockedItem.setStatus("UNLOCKED");
				lockedItem.setLastUpdateTime(new java.sql.Date(System.currentTimeMillis()));
				long t4 = System.currentTimeMillis();
				_logger.error("in Unlock inventory inner method step2 "+ (t4-t3));
			
				lockRepository.save(lockedItem);
				long t5 = System.currentTimeMillis();
				_logger.error("in Unlock inventory inner method step3 "+ (t5-t4));
			
				ProductStock productStock = new ProductStock();
				productStock.setInsertedOn(new Date());
				productStock.setIsActive(true);
				productStock.setProductCombinations(inventory);
				productStock.setUnitPrice(inventory.getPrice());
				productStock.setTotalStock(inventory.getAvailableStock());
				productStock.setTotalPrice(productStock.getUnitPrice()*productStock.getTotalStock());
				productStock.setOldStock(inventory.getAvailableStock() - Integer.parseInt(v));
				productStock.setStockChangeComment("Stock changed by unLockitems from cart "+lockedItem.getCartId()+", Old Stock was "+productStock.getOldStock()+" and new Stock is "+productStock.getTotalStock()+" "+remark);
				productStock.setUpdatedOn(new Date());
				productStockRepository.save(productStock);
				long t6 = System.currentTimeMillis();
				_logger.error("in Unlock inventory inner method step4 "+ (t6-t5));
			
				}catch (Exception e) {
					log.error("Exception while unlockedLockedIteam UNLOCKING the product for orderAttempt id "+lockedItem.getOrderAttemptId(),e);
				}
			});*/
			}else {
				_logger.error("error while unlocking the item of method unlockedLockedIteam with status REPROCEESSREQUIRED of OrderAttemptId "+lockedItem.getOrderAttemptId() ,lockedItem.toString());
				lockedItem.setActive(false);
				lockedItem.setStatus("REPROCEESSREQUIRED");
				lockedItem.setLastUpdateTime(new java.sql.Date(System.currentTimeMillis()));
				lockRepository.save(lockedItem);
				return;

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			_logger.error("error while unlocking the item of method unlockedLockedIteam with status  UNLOCKREQUIRED ->of OrderAttemptId "+lockedItem.getOrderAttemptId(),e);
			lockedItem.setActive(true);
			lockedItem.setStatus("UNLOCKREQUIRED");
			lockedItem.setLastUpdateTime(new java.sql.Date(System.currentTimeMillis()));
			lockRepository.save(lockedItem);
			return;
		}

//		lockedItem.setActive(false);
//		lockedItem.setStatus("UNLOCKED");
//		lockedItem.setLastUpdateTime(new java.sql.Date(System.currentTimeMillis()));
		lockRepository.delete(lockedItem);

	
		
	}
	
	

	public void sendToKafka(ProductInventoryBO pi, LockedItems lockedItem) {
		ObjectMapper _mapper = new ObjectMapper();
		try {
			if(StringUtils.isNotBlank(pi.getUniqueIdentifierForSearch())) {
			_logger.info("pushing  inventory updte information to kafka with id ............{}",pi.getUniqueIdentifierForSearch());
			String productInventryString = _mapper.writeValueAsString(pi);
			KafkaBrodcastingRequest kbr = new KafkaBrodcastingRequest("kb-catalog-data-change-topic",
					pi.getUniqueIdentifierForSearch(), productInventryString);
			kafkaBroadcastingService.broadcast(kbr);
			}
		} catch (Exception e) {
			_logger.error("Exception product inventory not send to kafka ............",e);
			lockedItem.setActive(true);
			lockedItem.setStatus("UNLOCKREQUIRED");
			lockedItem.setLastUpdateTime(new java.sql.Date(System.currentTimeMillis()));
			lockRepository.save(lockedItem);
			return;
		} 

	}
	
}
