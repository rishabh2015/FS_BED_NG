package com.kb.catalogInventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kb.catalogInventory.datatable.ProductView;
import com.kb.catalogInventory.datatable.SimilarProductDo;
import com.kb.catalogInventory.exception.InventoryException;
import com.kb.catalogInventory.model.ProductInventoryBO;
import com.kb.catalogInventory.model.SimilarProductBO;
import com.kb.catalogInventory.repository.ProductViewRepository;
import com.kb.catalogInventory.repository.SimilarProductRepository;
import com.kb.kafka.producer.KafkaBroadcastingService;
import com.kb.kafka.producer.KafkaBrodcastingRequest;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Log4j2
public class SimilarProductsService {

    @Autowired
    private SimilarProductRepository similarProductRepository;

    @Autowired
    private ProductViewRepository productViewRepository;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private KafkaBroadcastingService kafkaBroadcastingService;

    public void add(Map<String, List<String>> request) {
        ObjectMapper mapper = new ObjectMapper();
        request.forEach((k, v) -> {
            try {

                SimilarProductDo dto = similarProductRepository.findByProductCombinationUniqueId(k);
                if (null == dto) {
                    dto = new SimilarProductDo();
                    dto.setCreatedOn(new Date());
                } else {
                    if (StringUtils.isNotBlank(dto.getSimilarProductData())) {
                        List<String> prvSP = Arrays.asList(mapper.readValue(dto.getSimilarProductData(), String[].class));
                        v.addAll(prvSP);
                        Set<String> spSet= new HashSet<>(v);
                        v.clear();
                        v.addAll(spSet);
                    }
                }
                String similarProductListStr = mapper.writeValueAsString(v);
                dto.setProductCombinationUniqueId(k);
                dto.setSimilarProductData(similarProductListStr);
                dto.setUpdatedOn(new Date());
                similarProductRepository.save(dto);
            } catch (Exception e) {
                log.error("Error in similarProductService while adding for product id {}", k);
            }

        });


    }

    public List<SimilarProductBO> fetch(String productCombUuid) {
        SimilarProductDo dto = similarProductRepository.findByProductCombinationUniqueId(productCombUuid);
        ObjectMapper mapper = new ObjectMapper();
        List<SimilarProductBO> resList = new ArrayList<>();

        try {
            List<String> similarPcuuids = Arrays.asList(mapper.readValue(dto.getSimilarProductData(), String[].class));
            similarPcuuids.stream().forEach(uuid -> {
                try {
                    resList.add(productDetailService.similarProductMapper(uuid));
                } catch (InventoryException e) {
                    log.error("Error while fetching Similar Products inner for uuid {} is {}",productCombUuid, e);
                }
            });
        } catch (Exception e) {
        log.error("Error while fetching Similar Products outer for uuid {} is {}  ",productCombUuid,e );
        }
        return resList;
    }
    public void sendToKafka(Map<String,List<String>> rq) {
            log.info("Inside sendToKafka");
            ObjectMapper mapper= new ObjectMapper();



            try {
                String productInventryString = mapper.writeValueAsString(rq);
                KafkaBrodcastingRequest kbr = new KafkaBrodcastingRequest("kb-similar-prod-topic",
                        "similarProduct Key", productInventryString);
                kafkaBroadcastingService.broadcast(kbr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();

                log.error("JsonProcessingException sendToKafka ", e);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Exception sendToKafka ", e);
            }



    }
}
