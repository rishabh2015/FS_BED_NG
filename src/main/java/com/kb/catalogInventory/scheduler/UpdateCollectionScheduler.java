package com.kb.catalogInventory.scheduler;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.kb.catalogInventory.datatable.Collection;
import com.kb.catalogInventory.datatable.PageTypeStatus;
import com.kb.catalogInventory.datatable.SchedulerConfig;
import com.kb.catalogInventory.model.DynamicCollectionCondtion;
import com.kb.catalogInventory.model.DynamicCollectionRequest;
import com.kb.catalogInventory.repository.CollectionRepository;
import com.kb.catalogInventory.repository.PageTypeStatusRepository;
import com.kb.catalogInventory.repository.SchedulerConfigRepo;
import com.kb.catalogInventory.service.CollectionService;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class UpdateCollectionScheduler {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private PageTypeStatusRepository pageTypeStatusRepo;

    @Autowired
    private SchedulerConfigRepo schedulerConfigRepo;

    @Value("${run.scheduler}")
    private boolean runSchedulerChecking;

    @Scheduled(fixedDelay = 15 * 30 * 1000, initialDelay = 30000)
    public void run() {

        if (!runSchedulerChecking) {
            logger.warn("Not running UpdateCollectionScheduler");
            return;
        }


        List<Collection> collectionDoList = collectionRepository.findByTypeAndStatus("Automatic", 1);
        collectionDoList.stream().forEach(collectionDo -> {
            DynamicCollectionRequest dynamicCollectionRequest = new DynamicCollectionRequest();
            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode attributes = mapper.readTree(collectionDo.getAttributes());
                ObjectReader reader = mapper.readerFor(new TypeReference<List<DynamicCollectionCondtion>>() {
                });
                dynamicCollectionRequest.setAttributes(reader.readValue(attributes));
                dynamicCollectionRequest.setBannerUrl(collectionDo.getBannerUrl());
                dynamicCollectionRequest.setBlackListedCountries(collectionDo.getBlackListedCountries());
                dynamicCollectionRequest.setCollectionId(collectionDo.getId());
                dynamicCollectionRequest.setCondition(collectionDo.getCondition());
                dynamicCollectionRequest.setCountryRuleId(collectionDo.getProductCountryRule().getId());
                dynamicCollectionRequest.setHeroBanner(collectionDo.getHeroBanner());
                dynamicCollectionRequest.setIsFeatured(collectionDo.getIsFeatured());
                dynamicCollectionRequest.setLongDescription(collectionDo.getLongDescription());
                dynamicCollectionRequest.setRoundThumbnail(collectionDo.getRoundThumbnail());
                dynamicCollectionRequest.setShortDescription(collectionDo.getShortDescription());
                dynamicCollectionRequest.setSquareThumbnail(collectionDo.getSquareThumbnail());
                dynamicCollectionRequest.setStatus(collectionDo.getStatus());
                dynamicCollectionRequest.setTitle(collectionDo.getTitle());
                dynamicCollectionRequest.setType(collectionDo.getType());
                dynamicCollectionRequest.setWhiteListedCountries(collectionDo.getWhiteListedCountries());
                dynamicCollectionRequest.setIsBanner(collectionDo.getIsBanner());
                Optional<PageTypeStatus> ptsOptional = pageTypeStatusRepo.findById(collectionDo.getPageTypeId().longValue());
                if (ptsOptional.isPresent()) {
                    dynamicCollectionRequest.setPageTypeName(ptsOptional.get().getName());
                }
                collectionService.addDynamicCollection(dynamicCollectionRequest);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 30000)
    public void updateProductCombination() {
    	log.error("runSchedulerChecking  >>>> {} ",runSchedulerChecking);
		if (!runSchedulerChecking) {
			logger.warn("Not running updateProductCombination");
			return;
		}
		log.error("Inside Scheduler for updating the product combination");
        SchedulerConfig config = schedulerConfigRepo.findBySchedulerNameAndIsRun("CreateCollectionScheduler", true);
        if (config != null && config.isRun()) {
        	log.error("Inside IF Condition for  scheduler config");
        	if(System.currentTimeMillis()-config.getStartTime()>60000) {
        		log.error("Inside IF Condition for  execution time greater than 1 minute");
        		config.setProcessing(false);
                config.setRun(true);
                config.setLastExecutionTime(new Date());
                schedulerConfigRepo.save(config);
        	}
        config = schedulerConfigRepo.findBySchedulerNameAndIsRunAndIsProcessing("CreateCollectionScheduler", true,false);
        if (config != null && config.isRun()) {
        	try {
            	config.setStartTime(System.currentTimeMillis());
            	schedulerConfigRepo.save(config);
    			int result =	collectionActivityWithTimer(60*5*1000, config);
    			if(result==1) {
    				config.setProcessing(false);
                    config.setRun(false);
                    config.setLastExecutionTime(new Date());
                    schedulerConfigRepo.save(config);
    			}
    		} catch (InterruptedException e) {
    			  log.error("collectionActivityWithTimer InterruptedException ", e);
    				config.setProcessing(false);
                    config.setRun(true);
                    config.setLastExecutionTime(new Date());
                    schedulerConfigRepo.save(config);
    			
    		} catch (ExecutionException e) {
    			  log.error("collectionActivityWithTimer ExecutionException ", e);
    			config.setProcessing(false);
                config.setRun(true);
                config.setLastExecutionTime(new Date());
                schedulerConfigRepo.save(config);
    		
    		}
        }       
        	
        }

    }
    
    public int collectionActivityWithTimer(int timeinMillsecs, SchedulerConfig config) throws InterruptedException, ExecutionException {
    	int isDone = 0;
    	 Long t1 = System.currentTimeMillis();
    	  log.error("collectionActivityWithTimer started with time "+ timeinMillsecs);
    	ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<Integer> intCallable = () -> {
        	  if (config != null && config.isRun()){
                  log.error("Scheduler for updating the product combination started ");
                  config.setProcessing(true);
                  schedulerConfigRepo.save(config);

                  Optional<Collection> collectionOpt = collectionRepository.findById(config.getRecoredId());
                  if (collectionOpt.isPresent()) {
                      Collection collection = collectionOpt.get();
                      collectionService.updateProductCombination(collection.getProductSkus(), collection.getId());
                      if(StringUtils.isNotBlank(collection.getProductSkus())) {
                    	  collectionService.flushCollectionCache();
                      }
                      collectionService.updateProductOnKafka(collection);
                      config.setProcessing(false);
                      config.setRun(false);
                      config.setLastExecutionTime(new Date());
                      schedulerConfigRepo.save(config);
                  }

              }
        	  Long t2 = System.currentTimeMillis();
        	  logger.error("Collection task succeffully Done in millisecs "+ (t2-t1));
        	      
            return 1;
        };
        
        Future<Integer> intFuture = executorService.submit(intCallable);
        while(!intFuture.isDone()) {
            Thread.sleep(timeinMillsecs);
           
            intFuture.cancel(true);
        }

       isDone = intFuture.get();
       
       logger.error("Collection task has been interrupted after millisecs "+timeinMillsecs);
    	return isDone;
    }


}
