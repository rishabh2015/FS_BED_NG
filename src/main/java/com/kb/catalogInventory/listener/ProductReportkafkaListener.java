package com.kb.catalogInventory.listener;


import com.google.gson.Gson;
import com.kb.catalogInventory.model.ProductCacheBO;
import com.kb.catalogInventory.service.ProductDetailService;
import com.kb.catalogInventory.task.ReportGenerationTask;
import com.kb.catalogInventory.util.EmailService;
import com.kb.java.utils.RestApiSuccessResponse;
import com.kb.kafka.consumer.KafkaConsumerConfigs;
import com.kb.kafka.consumer.KafkaListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class ProductReportkafkaListener implements KafkaListener {

    @Value("${kafka.topic.for.product.report}")
    private String kafkaTopic;

    @Value("${kafka.consumer.groupid.for.product.report}")
    private String groupId;

    @Value("${report.recordsPerPage}")
    private int recordsPerPage;

    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    private EmailService emailService;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public KafkaConsumerConfigs getConsumerConfig() {
        KafkaConsumerConfigs kafkaConsumerConfigs = null;
        if ("uuid".equalsIgnoreCase(groupId)) {
            groupId = UUID.randomUUID().toString();
            kafkaConsumerConfigs = new KafkaConsumerConfigs(kafkaTopic, groupId, "earliest");
        } else {
            kafkaConsumerConfigs = new KafkaConsumerConfigs(kafkaTopic, groupId);
        }
        return kafkaConsumerConfigs;
    }

    @Override
    public void listen(ConsumerRecord<?, ?> consumerRecord) {
        String data = (String) consumerRecord.value();
        logger.info("Entered Product report listener : {}", data);
        try {
            Gson gson = new Gson();
            Map map = gson.fromJson(data, Map.class);
            String searchBy = map.get("searchBy") != null ? map.get("searchBy").toString() : "";
            String searchValue = map.get("searchValue") != null ? map.get("searchValue").toString() : "";
            String sortOrder = map.get("sortOrder") != null ? map.get("sortOrder").toString() : "";
            String displayCountryCode = map.get("displayCountryCode") != null ? map.get("displayCountryCode").toString() : "";
            String displayCurrencyCode = map.get("displayCurrencyCode") != null ? map.get("displayCurrencyCode").toString() : "";
            String mailId = map.get("mailId") != null ? map.get("mailId").toString() : "";
            Integer status = 0;
            try {
                status = map.get("status") != null ? Integer.parseInt(map.get("status").toString()) : 0;
            } catch (Exception e) {

            }
            fetchDataAndSendReport(searchBy, searchValue, status, sortOrder, displayCountryCode, displayCurrencyCode, mailId);
        } catch (Exception e) {
            logger.error("Exception came while sending report", e);
        }

    }

    public void fetchDataAndSendReport(String searchBy, String searchValue, int status, String sortOrder, String displayCountryCode, String displayCurrencyCode, String mailId) {
        new Thread(new ReportGenerationTask(searchBy, searchValue, status, sortOrder, displayCountryCode, displayCurrencyCode, mailId, productDetailService, emailService, recordsPerPage)).start();
    }

}

