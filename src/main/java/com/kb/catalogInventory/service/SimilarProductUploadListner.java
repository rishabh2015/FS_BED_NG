package com.kb.catalogInventory.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kb.catalogInventory.model.ProductInventoryRQ;
import com.kb.kafka.consumer.KafkaConsumerConfigs;
import com.kb.kafka.consumer.KafkaListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class SimilarProductUploadListner implements  KafkaListener{

	@Value("${kafka.topic.to.upload.similar.product}")
	private String kafkaTopic;
	
	@Value("${kafka.similar.prod.consumer.groupid}")
	private String groupId;
	
	
	
	@Autowired
	private SimilarProductsService similarProductsService;

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
		logger.info("Reached the consumer with key for similar products {}",data);
		ObjectMapper mapper = new ObjectMapper();

		try {
			Map<String,List<String>> inventories = mapper.readValue(data,
					new TypeReference<Map<String,List<String>>>() {
					});
			long t1= System.currentTimeMillis();
			logger.info("Entered similarProductUpload with RQ List : {}", inventories.toString());
//			Map<String,List<String>> req= new HashMap<>();
//			req.put((String)consumerRecord.key(),inventories);
			similarProductsService.add(inventories);
			long t2= System.currentTimeMillis();
			logger.info("Time taken in similarProductUpload with size --"+inventories.size()+" --- time taken --"+(t2-t1));
		} catch (Exception e) {
			logger.error("Exception came while similarProductUpload in kafka listner for data  ", data);
		}

	}
	
	
	

}
