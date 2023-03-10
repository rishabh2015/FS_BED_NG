package com.kb.catalogInventory.service;

import com.google.gson.Gson;
import com.kb.catalogInventory.model.BulkPriceBo;
import com.kb.catalogInventory.model.FinalPriceAndSupplierDetailModel;
import com.kb.catalogInventory.repository.PCForSchedulerRepository;
import com.kb.java.utils.KbRestTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class CacheAndBulkDataService {

    @Value("${admin.service.url}")
    private String adminServiceUrl;

    @Value("${bulk.price.fetch.url}")
    private String bulkPriceUrl;

    @Autowired
    private KbRestTemplate restTemplate;

    @Autowired
    private PCForSchedulerRepository repo;

    @Autowired
    private SupplierService supplierService;

    private static  Map<String,String> supplierCertiFicateMap= new HashMap<>();


     @PostConstruct
     private void populateCache(){
         getBulkSupplierSignature();
     }

       public String getSupplierCertiFicate(String supplierId){
           if(supplierCertiFicateMap.containsKey(supplierId)){
               return supplierCertiFicateMap.get(supplierId);

           }else{
          String sign=     getSingleSupplierSignature(supplierId);
               supplierCertiFicateMap.put(supplierId,sign);
               return sign;
           }
       }


    public String getSingleSupplierSignature(String supplierId) {
        final String url1 = adminServiceUrl.concat("supplier/getSupplierCertificate/" + supplierId);
        ResponseEntity<Map> response1 = restTemplate.exchange(url1, HttpMethod.GET, 60_000);
        Map<String, Object> respBody = response1.getBody();
        Map<String, Object> suppSig = (Map<String, Object>) respBody.get("supplierSignature");
        String supplierSignature = (String) suppSig.get("downloadableUrl");
        return supplierSignature;
    }


    private  void getBulkSupplierSignature() {
        final String url1 = adminServiceUrl.concat("supplier/getBulkSupplierCertificate" );
        ResponseEntity<Map> response1 = restTemplate.exchange(url1, HttpMethod.GET, 60_000);
        supplierCertiFicateMap = response1.getBody();
    }

    public Map<String, List<Map>>  getPriceInBulk(List<String> pcUUids){
        List<Object[]> pcList = repo.findDataOfPc(pcUUids);
        List<BulkPriceBo> bulkPriceRq=new ArrayList<>();
        List<String> pcs=new ArrayList<>();
        pcList.stream().forEach(rs->{
            BulkPriceBo bo= new BulkPriceBo();
            bo.setLowestProductCategoryName((String)rs[1]);
            bo.setProductCombinationId((String)rs[0]);
            bo.setQuantity(1);
            bulkPriceRq.add(bo);
            pcs.add((String)rs[0]);
        });
        Map<String, FinalPriceAndSupplierDetailModel> supplirResponse= supplierService.getsupplierDetailFromProduct(pcs);
        Map<String,Object> bulkRq=new HashMap<>();
        bulkRq.put("finalPriceRq",bulkPriceRq);
        bulkRq.put("supplierProductsData",supplirResponse);
        log.info("bulk price RQ from update scheduler {}",new Gson().toJson(bulkRq));

        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(bulkRq,headers);
        ResponseEntity<Map> bulkPriceRS= restTemplate.postForEntity(bulkPriceUrl,entity,60_000, Map.class);
        Map<String, List<Map>> res=(Map<String,List<Map>>)bulkPriceRS.getBody();
        return res;

    }
}
