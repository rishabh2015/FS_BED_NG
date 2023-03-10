package com.kb.catalogInventory.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kb.catalogInventory.datatable.ProductBulkUpdateException;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.repository.ProductBulkUpdateExceptionRepo;
import com.kb.catalogInventory.util.Utils;
import com.kb.java.utils.KbRunnable;
import com.kb.kafka.producer.KafkaBroadcastingService;
import com.kb.kafka.producer.KafkaBrodcastingRequest;

public class BulkPushThread extends KbRunnable {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	List<String> subListToProcess;

	private ProductUploadService productUploadService;

	String displayCountryCode;

	String displayCurrencyCode;

	long supplierId;
	
	private ProductBulkUpdateExceptionRepo exceptionRepo;
	
	private KafkaBroadcastingService kafkaBroadcastingService;
	private Map<String, Long> bestSellingProductCountDetailMap;
	
	private static final ObjectMapper _mapper = new ObjectMapper();

	public BulkPushThread(List<String> subList, String displayCountryCode, String displayCurrencyCode,
			ProductUploadService productUploadService, long supplierId, ProductBulkUpdateExceptionRepo exceptionRepo,
			KafkaBroadcastingService kafkaBroadcastingService, Map<String, Long> bestSellingProductCountDetailMap) {
		this.subListToProcess = subList;
		this.displayCountryCode = displayCountryCode;
		this.displayCurrencyCode = displayCurrencyCode;
		this.productUploadService = productUploadService;
		this.supplierId = supplierId;
		this.exceptionRepo = exceptionRepo;
		this.kafkaBroadcastingService = kafkaBroadcastingService;
		this.bestSellingProductCountDetailMap = bestSellingProductCountDetailMap;
	}

	@Override
	public void myRun() {
		final ProductBulkUpdateException exceptionOuter =new ProductBulkUpdateException();
		try {
			AtomicInteger counter = new AtomicInteger(1);
			exceptionOuter.setInsertedOn(new Date());
			subListToProcess.stream().forEach(uuuid -> {
				int count = counter.incrementAndGet();
				final ProductBulkUpdateException exception =new ProductBulkUpdateException();
				String exceptionMessage="";
				try {
					exception.setInsertedOn(new Date());
					exception.setUUUID(uuuid);
					exception.setApiName("bulkPushInventories");
					ProductInventoryBO bo=null;
					
					try {
					 bo=productUploadService.populateProductInventoryObj(uuuid, displayCountryCode,
							displayCurrencyCode, bestSellingProductCountDetailMap,null, "0",false);
					}catch (Exception e) {
						exception.setExceptionStage("Creating product Obj");
						exception.setRemark("Exception for creating prod obj for uuid "+uuuid);
						exception.setException(ExceptionUtils.getStackTrace(e));
						exception.setIsUpdated(false);
						exception.setFromApi(true);
						exception.setFromScheduler(false);
						exceptionMessage=e.getMessage();
						try {
							exceptionRepo.save(exception);
						} catch (Exception e1) {
							logger.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
							exception.setException(exceptionMessage);
							exceptionRepo.save(exception);
						}
					}
					if (null != bo) {
						sendToKafka(bo, exception);
					}
					logger.info("Sent to kafka  {}  object number -- {} \n", uuuid, count);
				} catch (Throwable e) {
					exception.setExceptionStage("Final catch ");
					exception.setRemark("Error sendToKafka with for supplier Id - " + supplierId + " count " + count + " UUID "
							+ uuuid);
					exception.setException(ExceptionUtils.getStackTrace(e));
					exception.setIsUpdated(false);
					exception.setFromApi(true);
					exception.setFromScheduler(false);
					exceptionMessage=e.getMessage();
					try {
						exceptionRepo.save(exception);
					} catch (Exception e1) {
						logger.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
						exception.setException(exceptionMessage);
						exceptionRepo.save(exception);
					}
					logger.error("Error sendToKafka with for supplier Id - " + supplierId + " count " + count + " UUID "
							+ uuuid + "----", e);

				}
				exception.setExceptionStage("Updated successfully !!!");
				exception.setRemark("NoError");
				exception.setException("N/A");
				exception.setIsUpdated(true);
				exception.setFromApi(true);
				exception.setFromScheduler(false);
				try {
				exceptionRepo.save(exception);
				}catch (Exception e) {
					logger.error("Exception occue while saving exception in ProductBulkUpdateException Table ",e);
					exception.setException(exceptionMessage);
					exceptionRepo.save(exception);
				}
			});
			
			logger.info("Successfully in pushing entire products for supplier Id - " + supplierId + " product count "
					+ this.subListToProcess.size() + "----");

		} catch (Exception e) {
			exceptionOuter.setExceptionStage("Exception before processing the sublist");
			exceptionOuter.setRemark("Exception in pushing entire products for supplier Id - " + supplierId + " product count "
					+ this.subListToProcess.size());
			exceptionOuter.setException(ExceptionUtils.getStackTrace(e));
			exceptionOuter.setIsUpdated(false);
			exceptionOuter.setFromApi(true);
			exceptionOuter.setFromScheduler(false);
			exceptionRepo.save(exceptionOuter);
			logger.error("Exception in pushing entire products for supplier Id - " + supplierId + " product count "
					+ this.subListToProcess.size() + "----", e);

		}
	}


	private void setRetailMarginAndBestSellingProductCount(ProductInventoryBO pi) {
		pi.getCountryWisePrice().forEach((key, value) ->
				{
					double retailMargin = Utils.calculateRetailMargin(value.getProductMrp(), value.getFinalPricePerUnit(), value.getSetPieces());
					logger.info("productId:[{}] -- retailMargin:[{}]", pi.getProductString(), retailMargin);
					value.setRetailMargin(retailMargin);
				});
		pi.setBestSellingProductCount(bestSellingProductCountDetailMap.getOrDefault(pi.getUniqueIdentifierForSearch().trim(), 0L));
	}
	
	public void sendToKafka(ProductInventoryBO pi, ProductBulkUpdateException exception) {
       if(null!=pi && pi.getImages()!=null && pi.getImages().size() >0) {
		logger.info("Inside sendToKafka");
		String exceptionMessage="";
		try {
			String productInventryString = _mapper.writeValueAsString(pi);
			KafkaBrodcastingRequest kbr = new KafkaBrodcastingRequest("kb-catalog-data-topic",
					pi.getUniqueIdentifierForSearch(), productInventryString);
			kafkaBroadcastingService.broadcast(kbr);
		} catch (JsonProcessingException e) {
			exception.setExceptionStage("Sending to kafka");
			exception.setRemark("JsonProcessingException");
			exception.setException(ExceptionUtils.getStackTrace(e));
			exception.setIsUpdated(false);
			exception.setFromApi(true);
			exception.setFromScheduler(false);
			exceptionMessage=e.getMessage();
			try {
				exceptionRepo.save(exception);
			} catch (Exception e1) {
				logger.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
				exception.setException(exceptionMessage);
				exceptionRepo.save(exception);
			}

			logger.error("JsonProcessingException sendToKafka ",e);
		} catch (Exception e) {
			exception.setExceptionStage("Sending to kafka");
			exception.setRemark("Exception other than json processing");
			exception.setException(ExceptionUtils.getStackTrace(e));
			exception.setIsUpdated(false);
			exception.setFromApi(true);
			exception.setFromScheduler(false);
			exceptionMessage=e.getMessage();
			try {
				exceptionRepo.save(exception);
			} catch (Exception e1) {
				logger.error("Exception occue while saving exception in ProductBulkUpdateException Table ", e1);
				exception.setException(exceptionMessage);
				exceptionRepo.save(exception);
			}
			logger.error("Exception sendToKafka ",e);
		}

		} else {
			if (null != pi) {
				exception.setExceptionStage("Sending to kafka");
				exception.setRemark("Exception Inside bulk push inventory  images are not present for productCId ---"
						+ pi.getUniqueIdentifierForSearch());
				exception.setException("Exception Inside bulk push inventory  images are not present for productCId ---"
						+ pi.getUniqueIdentifierForSearch());
				exception.setIsUpdated(false);
				exception.setFromApi(true);
				exception.setFromScheduler(false);
				exceptionRepo.save(exception);
				logger.error("Exception Inside bulk push inventory  images are not present for productCId ---"
						+ pi.getUniqueIdentifierForSearch());
			}

		}
       
	}
}