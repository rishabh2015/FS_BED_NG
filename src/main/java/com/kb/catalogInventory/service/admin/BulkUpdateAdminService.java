package com.kb.catalogInventory.service.admin;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import com.kb.catalogInventory.model.BestSellingRQ;
import com.kb.catalogInventory.util.Utils;
import com.kb.java.utils.KbRestTemplate;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.repository.ProductBulkUpdateExceptionRepo;
import com.kb.catalogInventory.repository.ProductCombinationsRepository;
import com.kb.catalogInventory.repository.ProductRepository;
import com.kb.catalogInventory.repository.SupplierRepository;
import com.kb.catalogInventory.service.BulkPushThread;
import com.kb.catalogInventory.service.ProductMapper;
import com.kb.catalogInventory.service.ProductUploadService;
import com.kb.kafka.producer.KafkaBroadcastingService;
import com.kb.kafka.producer.KafkaBrodcastingRequest;
import org.springframework.util.CollectionUtils;

@Component
public class BulkUpdateAdminService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ProductCombinationsRepository combinationRepo;

	@Autowired
	private ProductRepository productRepo;

	@Autowired
	private SupplierRepository supplierRepo;

	@Autowired
	private ProductUploadService productUploadService;

	@Autowired
	private ProductMapper mapper;

	private static ThreadPoolTaskExecutor threadPoolExecutor = null;

	@Value("${order.service.bestselling.getsellingcountdetail.url}")
	private String getSellingCountDetailsUrl;

	@Value("${order.service.bestselling.getsellingcountdetail.url.timeout}")
	private int getSellingCountDetailsUrlTimeout;

	@Autowired
	KbRestTemplate restTemplate;

	@Autowired
	private ProductBulkUpdateExceptionRepo exceptionRepo;
	
	@Autowired
	private KafkaBroadcastingService kafkaBroadcastingService;

	private static final ObjectMapper _mapper = new ObjectMapper();
	
	

	@PostConstruct
	public void postContruct() {
		threadPoolExecutor = new ThreadPoolTaskExecutor();
		threadPoolExecutor.setCorePoolSize(10);
		threadPoolExecutor.setQueueCapacity(100);
		threadPoolExecutor.setMaxPoolSize(20);
		threadPoolExecutor.initialize();
	}

	public void updateAllUuidsOfProductCombination() {

		logger.info("Inside updateAllUuidsOfProductCombination");
		combinationRepo.findAll().stream().forEach(pc -> {
			pc.setUniqueIdentifier(UUID.randomUUID().toString());
			combinationRepo.save(pc);
		});

	}

	public void bulkPushInventoriesToKafka(String displayCountryCode, String displayCurrencyCode) {

		logger.info("Inside bulkPushInventoriesToKafka with display country code {}", displayCountryCode);
		List<String> uuids = combinationRepo.findAllActiveUniqueIdentifiers();
		logger.info("Going for bulk upload with product   SIZE----------->  {}", uuids.size());
		logger.info("Going for bulk upload with product   SIZE----------->  {} \n", uuids.size());
		AtomicInteger counter = new AtomicInteger(1);
		Map<String, Long> bestSellingProductCountDetailMap = new HashMap<>();
//		Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(uuids);

		uuids.stream().forEach(uuuid -> {
			int count = counter.incrementAndGet();
			try {
				sendToKafka(productUploadService.populateProductInventoryObj(uuuid, displayCountryCode,
						displayCurrencyCode, bestSellingProductCountDetailMap,null, "0",false));
				logger.info("Sent to kafka  {}  object number -- {} \n", uuuid, count);
			} catch (Throwable e) {

				logger.error("Error bulkPushInventoriesToKafka with counter - " + count, e);

			}
		});
	}

	public void bulkPushInventoriesToKafkaWithMuliThread(String displayCountryCode, String displayCurrencyCode) {

		logger.info("Inside bulkPushInventoriesToKafka with display country code {}", displayCountryCode);
		List<Long> listOfSuppliers = combinationRepo.listOfActiveProductSuppliers();

		listOfSuppliers.stream().forEach(supplierId -> {
			pushSupplierProductToKafkaInThread(supplierId, displayCountryCode, displayCurrencyCode);

		});

	}

	public ProductInventoryBO pushProductToKafka(String uniqueIdentifier, String displayCountryCode,
												 String displayCurrencyCode) {
		ProductInventoryBO pi = null;
		try {
			Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(Collections.singletonList(uniqueIdentifier));
			pi = productUploadService.populateProductInventoryObj(uniqueIdentifier, displayCountryCode,
					displayCurrencyCode, bestSellingProductCountDetailMap,null, "0",false);
		} catch (Exception e) {
			logger.error("Error while creting product ", e);
		}
		if (null != pi) {
			sendToKafka(pi);
		}
		return pi;
	}

	public String pushProductToKafkaByUUIDs(List<String> productIds, String displayCountryCode,
											String displayCurrencyCode) throws Exception {
		if (!CollectionUtils.isEmpty(productIds)) {
			Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productIds);
			productIds.forEach(productId -> {
				ProductInventoryBO productInventoryBO = null;
				try {
					productInventoryBO = productUploadService.populateProductInventoryObj(productId, displayCountryCode,
							displayCurrencyCode, bestSellingProductCountDetailMap, null, "0", false);

				} catch (Exception e) {
					logger.error("Error while creting product ", e);
				}
				if (null != productInventoryBO) {
					sendToKafka(productInventoryBO);
				}
			});
		}
		return "Success";
	}

	private void setRetailMarginAndBestSellingProductCount(ProductInventoryBO pi) {
		pi.getCountryWisePrice().forEach((key, value) -> {
			double retailMargin = Utils.calculateRetailMargin(value.getProductMrp(), value.getFinalPricePerUnit(), value.getSetPieces());
					logger.info("productId:[{}] ---- retailMargin:[{}]", pi.getProductString(), retailMargin);
					value.setRetailMargin(retailMargin);
		});
		Map<String, Long> bestSellingProductCountDetailMap = new HashMap<>();
//		Map<String, Long> bestSellingProductCountDetailMap =
//				fetchBestSellingProductDetailCount(Collections.singletonList(pi.getUniqueIdentifierForSearch().trim()));
		pi.setBestSellingProductCount(bestSellingProductCountDetailMap.getOrDefault(pi.getUniqueIdentifierForSearch().trim(), 0L));
	}


	public String pushSupplierProductToKafka(Long supplierId, String displayCountryCode, String displayCurrencyCode) {
		AtomicInteger counter = new AtomicInteger(1);
		combinationRepo.findBySupplier(supplierRepo.findByIdAndIsActive(supplierId, true)).stream().forEach(pc -> {
			if (pc.getAvailableStock() > 0) {
				int count = counter.incrementAndGet();
				try {
					Map<String, Long> bestSellingProductCountDetailMap = new HashMap<>();
//					Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(Collections.singletonList(pc.getUniqueIdentifier()));
					sendToKafka(productUploadService.populateProductInventoryObj(pc.getUniqueIdentifier(),
							displayCountryCode, displayCurrencyCode, bestSellingProductCountDetailMap,null, "0",false));
					logger.info("Sent to kafka  {}  object number -- {} \n", pc.getUniqueIdentifier(), count);
				} catch (Throwable e) {

					logger.error("Error bulkPushInventoriesToKafka with counter - " + count, e);

				}
			}
		});

		return "Updated for supplier";
	}

	public String pushSupplierProductToKafkaInThread(Long supplierId, String displayCountryCode,
			String displayCurrencyCode) {
		List<String> productList = combinationRepo.findUniqueIdentifiersBySupplier(supplierId);
		Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productList);
		BulkPushThread supplireThread = new BulkPushThread(productList,  displayCountryCode, displayCurrencyCode,
				productUploadService, supplierId,exceptionRepo,kafkaBroadcastingService, bestSellingProductCountDetailMap);

		try{
			threadPoolExecutor.submit(supplireThread);
		}catch (Exception e){
			logger.error("Exception came while submitting thread for supplier with supplier id so processing it in forground: "+supplierId,e);
			supplireThread.myRun();
		}

		return "Updated for supplier";
	}

	public void updateAllUuidsOfProduct() {
		logger.info("Inside updateAllUuidsOfProduct");
		productRepo.findAll().stream().forEach(p -> {
			p.setUUID(UUID.randomUUID().toString());
			productRepo.save(p);
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
			logger.error("Exception occurred while data parsing of bestSellingProductDetails", e);
		}
		return outputMap;
	}

	

	public void sendToKafka(ProductInventoryBO pi) {
		if (null != pi && pi.getImages() != null && pi.getImages().size() > 0) {
			logger.info("Inside sendToKafka");
			try {
				String productInventryString = _mapper.writeValueAsString(pi);
				KafkaBrodcastingRequest kbr = new KafkaBrodcastingRequest("kb-catalog-data-topic",
						pi.getUniqueIdentifierForSearch(), productInventryString);
				kafkaBroadcastingService.broadcast(kbr);
			} catch (JsonProcessingException e) {
				e.printStackTrace();

				logger.error("JsonProcessingException sendToKafka ", e);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Exception sendToKafka ", e);
			}

		} else {
			if (null != pi) {
				logger.error("Exception Inside bulk push inventory  images are not present for productCId ---"
						+ pi.getUniqueIdentifierForSearch());
			}

		}

	}
}