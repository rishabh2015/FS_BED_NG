package com.kb.catalogInventory.scheduler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import com.kb.catalogInventory.model.RemoveProductBO;
import com.kb.catalogInventory.repository.PCForSchedulerRepository;
import com.kb.catalogInventory.repository.ProductCombinationsArchiveRepository;
import com.kb.catalogInventory.repository.ProductCombinationsRepository;
import com.kb.catalogInventory.service.ProductStockService;

@Configuration
@EnableScheduling
public class SchedulerTask {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ProductStockService stockService;
	
	@Autowired
	private PCForSchedulerRepository productCombRepository;
	
	
	@Autowired
	private ProductCombinationsArchiveRepository productCombArchRepository;

	@Value("${run.scheduler}")
	private boolean runScheduler;
	
	
    @Scheduled(fixedRate = 900000)
	public void sendInactiveProductToKafka() {

		if(!runScheduler){
			logger.warn("Not running pushUpdatedProductToKafka scheduler");
			return;
		}

		List<Object[]> resultSet = productCombRepository.findInactiveProductByUpdatedDate(1);
		
		
		//select count(*) from kb_catalog_inventory.product_combinations_Archive  where updated_on > sysdate()-7
		List<Object[]> resultSetArch=productCombArchRepository.findInactiveProductByUpdatedDate(1);
		
		resultSet.addAll(resultSetArch);
		
		
		
		
		if (!CollectionUtils.isEmpty(resultSet)) {
			resultSet.stream().forEach(pc -> {
				logger.info("Document with id should get deleted from elasticsearch : " + pc[0].toString());
				RemoveProductBO bo = RemoveProductBO.builder().productCombinationId(pc[0].toString())
						.availableStock(Integer.valueOf(pc[2].toString())).removeProductFromList(true).build();
				stockService.sendToKafka(bo,true);
			});
		} else {
			logger.info("No record found for purge the document from elasticsearch.");
		}
	}
}