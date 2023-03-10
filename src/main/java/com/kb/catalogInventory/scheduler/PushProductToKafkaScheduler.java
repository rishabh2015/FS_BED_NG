package com.kb.catalogInventory.scheduler;

import java.util.*;
import java.util.stream.Collectors;

import com.kb.catalogInventory.datatable.ProductView;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kb.catalogInventory.datatable.ProductBulkUpdateException;
import com.kb.catalogInventory.datatable.ProductCombinations;
import com.kb.catalogInventory.datatable.ProductStatus;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.model.StatusConstant;
import com.kb.catalogInventory.repository.ProductBulkUpdateExceptionRepo;
import com.kb.catalogInventory.repository.ProductCombinationsRepository;
import com.kb.catalogInventory.repository.ProductStatusRepository;
import com.kb.catalogInventory.service.ProductUploadService;
import com.kb.kafka.producer.KafkaBroadcastingService;
import com.kb.kafka.producer.KafkaBrodcastingRequest;

@Component
public class PushProductToKafkaScheduler {
	
	private final static Logger _logger = LoggerFactory.getLogger(PushProductToKafkaScheduler.class);

	@Autowired
	private ProductCombinationsRepository inventoryRepository;
	
	@Autowired
	private ProductUploadService productUploadService;
	
	private static final ObjectMapper _mapper = new ObjectMapper();
	
	@Autowired
	KafkaBroadcastingService kafkaBroadcastingService;
	
	@Autowired
	private ProductStatusRepository productStatusRepository;
	
	@Autowired
	private ProductBulkUpdateExceptionRepo exceptionRepo;

	@Value("${order.service.bestselling.getsellingcountdetail.url}")
	private String getSellingCountDetailsUrl;

	@Value("${order.service.bestselling.getsellingcountdetail.url.timeout}")
	private int getSellingCountDetailsUrlTimeout;

	@Autowired
	KbRestTemplate restTemplate;

	@Value("${run.scheduler}")
	private boolean runScheduler;

	@Scheduled(fixedDelay = 300000l)
    public void pushQCDoneProductToKafka() {

		if(!runScheduler){
			_logger.warn("Not running pushProductToKafka scheduler");
			return;
		}

		Optional<ProductStatus> ps = productStatusRepository.findById(StatusConstant.QC_Done);
		List<ProductCombinations> pcist=inventoryRepository.findByProductStatus(ps.get());
		List<String> productUUIDList = pcist.stream().map(ProductCombinations:: getUniqueIdentifier).collect(Collectors.toList());
		Map<String, Long> bestSellingProductCountDetailMap = fetchBestSellingProductDetailCount(productUUIDList);
		pcist.stream().forEach(pc->{
			final ProductBulkUpdateException exception =new ProductBulkUpdateException();
			String exceptionMessage="";
			exception.setInsertedOn(new Date());
			exception.setUUUID(pc.getUniqueIdentifier());
			ProductInventoryBO bo=null;
			try {
				bo = productUploadService.populateProductInventoryObj(pc.getUniqueIdentifier(),"IN","INR", bestSellingProductCountDetailMap,null, "0",false);
			} catch (Exception e) {
				_logger.error("Error while creating product obj :{}",pc.getUniqueIdentifier(),e);
				exception.setExceptionStage("Creating product Obj");
				exception.setRemark("Exception for creating prod obj for uuid "+pc.getUniqueIdentifier());
				exception.setException(e.getMessage());
				exception.setIsUpdated(false);
				exception.setFromApi(false);
				exception.setFromScheduler(true);
				exception.setSchedulerName("PushProductToKafkaScheduler");
				exceptionMessage=e.getMessage();
				try {
					exceptionRepo.save(exception);
				} catch (Exception e1) {
					_logger.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
					exception.setException(exceptionMessage);
					exceptionRepo.save(exception);
				}
			}
			pc.setStatus(ps.get());
			if(null!=bo && null!=bo.getPriceMatrix() && !bo.getPriceMatrix().isEmpty() && null!=bo.getCountryWisePrice() && bo.getCountryWisePrice().size()>0) {
			sendToKafka(bo,exception);
			}
			exception.setExceptionStage("Updated successfully !!");
			exception.setRemark("Product Updated for uuid "+pc.getUniqueIdentifier());
			exception.setException("N/A");
			exception.setIsUpdated(true);
			exception.setFromApi(false);
			exception.setFromScheduler(true);
			exception.setSchedulerName("PushProductToKafkaScheduler");
			exceptionRepo.save(exception);

			inventoryRepository.save(pc);
		});
		
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
		} catch (Exception e) {
			_logger.error("Exception occurred while data parsing of bestSellingProductDetails", e);
		}
		return outputMap;
	}
	
	public void sendToKafka(ProductInventoryBO pi, ProductBulkUpdateException exception) {
		String exceptionMessage="";
		try {
			if(StringUtils.isNotBlank(pi.getUniqueIdentifierForSearch()) && pi.getImages()!=null && pi.getImages().size() >0) {
			_logger.info("pushing  pc  to kafka with id ............{}",pi.getUniqueIdentifierForSearch());
			String productInventryString = _mapper.writeValueAsString(pi);
			KafkaBrodcastingRequest kbr = new KafkaBrodcastingRequest("kb-catalog-data-topic",
					pi.getUniqueIdentifierForSearch(), productInventryString);
			kafkaBroadcastingService.broadcast(kbr);
			}else {
				_logger.error(" either images are not present or pcUUId is not present  ");
				exception.setExceptionStage("Sending object to kafka");
				exception.setRemark("Exception for creating prod obj for uuid "+pi.getUniqueIdentifierForSearch());
				exception.setException(" either images are not present or pcUUId is not present  ");
				exception.setIsUpdated(false);
				exception.setFromApi(false);
				exception.setFromScheduler(true);
				exception.setSchedulerName("PushProductToKafkaScheduler");
				exceptionMessage=" either images are not present or pcUUId is not present  ";
				try {
					exceptionRepo.save(exception);
				} catch (Exception e1) {
					_logger.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
					exception.setException(exceptionMessage);
					exceptionRepo.save(exception);
				}

			}
		} catch (JsonProcessingException e) {
			_logger.error("JsonProcessingException product inventory not send to kafka ............",e);
			exception.setExceptionStage("Sending object to kafka");
			exception.setRemark("JsonProcessingException product inventory not send to kafka for uuid "+pi.getUniqueIdentifierForSearch());
			exception.setException(e.getMessage());
			exception.setIsUpdated(false);
			exception.setFromApi(false);
			exception.setFromScheduler(true);
			exception.setSchedulerName("PushProductToKafkaScheduler");
			exceptionMessage=" JsonProcessingException ";
			try {
				exceptionRepo.save(exception);
			} catch (Exception e1) {
				_logger.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
				exception.setException(exceptionMessage);
				exceptionRepo.save(exception);
			}

		} catch (Exception e) {
			_logger.error("Exception product inventory not send to kafka ............",e);
			exception.setExceptionStage("Sending object to kafka");
			exception.setRemark("Outer Exception send to kafka for uuid "+pi.getUniqueIdentifierForSearch());
			exception.setException(e.getMessage());
			exception.setIsUpdated(false);
			exception.setFromApi(false);
			exception.setFromScheduler(true);
			exception.setSchedulerName("PushProductToKafkaScheduler");
			exceptionMessage="Outer Exception";
			try {
				exceptionRepo.save(exception);
			} catch (Exception e1) {
				_logger.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
				exception.setException(exceptionMessage);
				exceptionRepo.save(exception);
			}


		} 

	}
}
