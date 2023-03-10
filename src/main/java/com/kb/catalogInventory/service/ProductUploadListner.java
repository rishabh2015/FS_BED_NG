package com.kb.catalogInventory.service;

import java.util.List;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kb.catalogInventory.model.ProductInventoryRQ;
import com.kb.kafka.consumer.KafkaConsumerConfigs;
import com.kb.kafka.consumer.KafkaListener;

@Component
public class ProductUploadListner implements  KafkaListener{

	@Value("${kafka.topic.for.product.upload}")
	private String kafkaTopic;
	
	@Value("${kafka.consumer.groupid}")
	private String groupId;
	
	
	
	@Autowired
	private ProductUploadService productUploadService;

	Logger logger  = LoggerFactory.getLogger(this.getClass());

	
	@Override
	public KafkaConsumerConfigs getConsumerConfig() {
		KafkaConsumerConfigs kafkaConsumerConfigs = null;
		if("uuid".equalsIgnoreCase(groupId)){
			groupId = UUID.randomUUID().toString();
			kafkaConsumerConfigs  = new KafkaConsumerConfigs(kafkaTopic, groupId,"earliest");
		}else{
			kafkaConsumerConfigs = new KafkaConsumerConfigs(kafkaTopic, groupId);
		}
		return kafkaConsumerConfigs;
	}

	@Override
	public void listen(ConsumerRecord<?, ?> consumerRecord) {
		String data = (String) consumerRecord.value();
		ObjectMapper mapper = new ObjectMapper();

		try {
			List<ProductInventoryRQ> inventories = mapper.readValue(data,
					new TypeReference<List<ProductInventoryRQ>>() {
					});
			long t1= System.currentTimeMillis();
			logger.info("Entered uploadProduct with ProductInventoryRQ List : {}", inventories.toString());
			List<String> res=productUploadService.populateProductInventoryBulk(inventories);
			long t2= System.currentTimeMillis();
			logger.info("Time taken in uploading with size --"+inventories.size()+" --- time taken --"+(t2-t1));
		} catch (Exception e) {
			logger.error("Exception came while uploading product", e);
		}

	}
	
	
	

}
